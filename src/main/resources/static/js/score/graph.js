const getAjax = function(url) {
    return new Promise((resolve, reject) => { // 1.
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json"
            ,
            success: (res) => {
                resolve(res);  // 2.
            },
            error: (e) => {
                reject(e);  // 3.
            }
        });
    });
}


async function getRank(affiliation){
    const url = "/rank-graph/"+affiliation;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}
async function getRankCurrent(affiliation){
    const url = "/rank-graph-current/"+affiliation;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}

async function getRankHistory(userId){
    const url = "/rank-graph-history/"+userId;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}


async function createRankGraph(affiliation){
    let rankData = await getRankCurrent(affiliation);

    let userDict = {};
    let historyCount = 0;
    let dateStringList = [];
    let graphLabel = [];


    const dateNow = new Date();
    for(let i = 0; i< rankData.length; i++){
        let history = await getRankHistory(rankData[i].userId);
        userDict[i+1] = {"nickName": rankData[i].nickName, "history":history, "score": [rankData[i].totalScore]};
        if(historyCount < history.length){
            historyCount = history.length;
        }
    }
    await console.log(userDict);
    for (let i = 0; i < historyCount; i++) {
        const timeAgo = new Date(dateNow.getTime() - (i * 60 * 60 * 1000));

        timeAgo.setMinutes(0);
        timeAgo.setSeconds(0);

        const year = timeAgo.getFullYear();
        const month = (timeAgo.getMonth() + 1).toString().padStart(2,"0");
        const day = (timeAgo.getDate()).toString().padStart(2,"0");
        const hours = (timeAgo.getHours()).toString().padStart(2,"0");

        dateStringList.push(`${year}-${month}-${day}T${hours}:00:00`);
        graphLabel.push(`${hours}:00`);
    }

    for(let i = 0; i<Object.keys(userDict).length; i++){
        for(let j= 0; j<dateStringList.length; j++){
            for(let k = 0; k<Object.keys(userDict[i+1].history).length; k++){
                if(dateStringList[j] === userDict[i+1].history[k].dateTime){
                    userDict[i+1].score.push(userDict[i+1].history[k].score);
                    break;
                }
                if(dateStringList[j] !== userDict[i+1].history[k].dateTime && k === Object.keys(userDict[i+1].history).length -1 ){
                    console.log(userDict[i+1].history[k])
                    userDict[i+1].score.push(null);
                }
            }
        }
    }
    await console.log(userDict);
    const hour = (dateNow.getHours()).toString().padStart(2,"0");
    const min = (dateNow.getMinutes()).toString().padStart(2,"0");
    graphLabel.reverse()
    graphLabel.push(`${hour}:${min}`);



    const redColor =["#FF7A7A", "#FF4646","#FC0000", "#A61804", "#5D0D02"];
    const blueColor = ["#015C92", "#2D82B5","#53A7D8","#88CDF6","#BCE6FF"];
    const color1 =["#33539E", "#7FACD6", "#BFB8DA", "#E8B7D4", "#A5678E"]
    const color2 = ["#ff7b89", "#8a5082", "#6f5f90", "#758eb7", "#a5cad2"];

    const testData =[[0,200],[0,300],[0,400],[0,500],[0,600]]
    const ctx = document.getElementById('rank-chart').getContext('2d');
    let dataSet = [];
    for(let i = 0; i<Object.keys(userDict).length; i++){
        dataSet.push({
           label: userDict[i+1].nickName,
           data: userDict[i+1].score.reverse(),
            borderColor: color1[i],
            tension: 0.1,
            fill: false
        });
    }
    const data = {
        labels: graphLabel,
        datasets: dataSet
    };

    const options = {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    };
    new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });







}