async function showPopup(id) {
    try {
        const quizUrl = "/quiz/" + id
        const quiz = await getData(quizUrl).catch(error=>{
        if(error.responseText == 'notOpen'){
            return "notOpen"
        }else if (error.responseText == 'ValidationError'){
            return "ValidationError"
        }else {
            return "undefined"
        }
        })
        const postUrl = "/challenge/" + id
        const rankUrl = '/rank/challenge/'+id
        //quiz가 값이 필요함
        if(quiz == "ValidationError"){
            alert("Validation Error")
        }
        else if(quiz=="undefined"){
            alert("Error")
        }
        else {

            //Div Element 추가

            let challengeContentDiv = document.createElement('input')
            let challengeContentButton = document.createElement('label');
            let challengeRankDiv = document.createElement('input');
            let challengeRankButton = document.createElement('label')
            let contentDiv= document.createElement('div');

            let nameDiv = document.createElement('div');
            let titleFlexDiv = document.createElement('div');
            let titleWriterDiv = document.createElement('h3');
            let writerDiv = document.createElement('div');
            let scoreDiv = document.createElement('div');
            let titleDescriptionDiv = document.createElement('h3');
            let descriptionDiv = document.createElement('div');
            let flagDiv = document.createElement("input");
            let buttonDiv = document.createElement("button");

            let flagFlexDiv = document.createElement('div');

            let attatchmentDiv = document.createElement('a');
            let titleAttatchmentDiv = document.createElement("h3");


            let rankTableDiv = document.createElement('table');
            let rankTableTitle = document.createElement('thead');
            let rankTableTitleTr = document.createElement('tr')
            let rankTableTitleName = document.createElement('td');
            let rankTableTitleRank = document.createElement('td');
            let rankTableTitleTime=document.createElement('td');

            let rankTableContent = document.createElement('tr');
            let rankTableContentName = document.createElement('td');
            let rankTableContentRank = document.createElement('td');
            let rankTableContentTime = document.createElement('td');




            titleFlexDiv.className="titleFlex"
            flagFlexDiv.className='flagFlex'

            contentDiv.id='contentDiv'

            //quiz정보 입력
            titleWriterDiv.innerText = "Author : "
            titleDescriptionDiv.innerText = "Description"

            writerDiv.className = 'popupBoxContent';
            scoreDiv.className = 'popupBoxTitle';
            scoreDiv.id= "popupTitle"
            descriptionDiv.className = 'popupBoxContent description';



            nameDiv.className = 'popupBoxTitle';
            nameDiv.id="popupTitle"
            titleWriterDiv.className = 'popupBoxTitle';
            titleDescriptionDiv.className = 'popupBoxTitle'


            scoreDiv.style="font-size : 2em;"

            flagDiv.className="popupFlagInput";
            flagDiv.placeholder="Flag 입력"
            // descriptionDiv.className="description"
            // titleFlag.className="flag-title";

            nameDiv.innerText = quiz.quizName;
            writerDiv.innerText = quiz.author;
            scoreDiv.innerText = quiz.score;
            descriptionDiv.innerText = quiz.description;
            buttonDiv.innerText = "Submit";
            buttonDiv.className="myButton"

            rankTableDiv.className='rankTableDiv'
            rankTableDiv.id='rankTableDiv'
            rankTableTitle.className='rankTableTitle'
            rankTableTitleTr.className='rankTableTitleTr'
            rankTableTitleName.className='rankTableTitleName'
            rankTableTitleRank.className='rankTableTitleRank'
            rankTableTitleTime.className='rankTableTitleTime'
            rankTableContent.className='rankTableContent'
            rankTableContentName.className='rankTableContentName'
            rankTableContentRank.className='rankTableContentRank'
            rankTableContentTime.className='rankTableContentTime'

            rankTableTitleName.innerText="NickName"
            rankTableTitleRank.innerText="Rank"
            rankTableTitleTime.innerText='Solved Time'

            rankTableTitleTr.appendChild(rankTableTitleRank)
            rankTableTitleTr.appendChild(rankTableTitleName)
            rankTableTitleTr.appendChild(rankTableTitleTime)
            rankTableTitle.appendChild(rankTableTitleTr);
            rankTableDiv.appendChild(rankTableTitle);
            rankTableDiv.appendChild(rankTableContent);

            //버튼 추가
            challengeContentDiv.className="content-button";
            challengeContentDiv.autocomplete="off";
            challengeContentDiv.onclick= function showChallenge() {
                document.getElementById('contentDiv').style.display = 'block';
                document.getElementById('rankTableDiv').style.display='none';
                document.getElementById('chB').style="background : white ;color: black;";
                document.getElementById('raB').style="background : transparent;margin-left: 5%;"
            }
            challengeContentDiv.checked=true;
            challengeContentDiv.className='btn-check';
            challengeContentDiv.name='ch'
            challengeContentDiv.id='ch'

            challengeContentButton.id='chB'
            challengeContentButton.className='change-challenge-rank'
            challengeContentButton.style="background : white ;color: black;";
            challengeContentButton.htmlFor='ch'
            challengeContentButton.innerText='Challenge'


            challengeRankDiv.className="content-button";
            challengeRankDiv.autocomplete="off";
            challengeRankDiv.onclick=  async function showRankTable() {
                const rank = await getData(rankUrl);
                document.getElementById('contentDiv').style.display = 'none';
                document.getElementById('rankTableDiv').style.display='block';
                for(let i=0; i < rank.length; i++){
                    rankTableContentRank.innerText= i+1;
                    rankTableContentName.innerText = rank[i].nickName;
                    rankTableContentTime.innerText = rank[i].solvedTime;
                    rankTableContent.appendChild(rankTableContentRank)
                    rankTableContent.appendChild(rankTableContentName)
                    rankTableContent.appendChild(rankTableContentTime)
                }
                document.getElementById('raB').style="background : white ;color: black; margin-left: 5%;";
                document.getElementById('chB').style="background : transparent;"
            }
            challengeRankDiv.className='btn-check';
            challengeRankDiv.name='ra'
            challengeRankDiv.id='ra'

            challengeRankButton.id='raB'
            challengeRankButton.className='change-challenge-rank'
            challengeRankButton.htmlFor='ra'
            challengeRankButton.style='margin-left: 5%;'
            challengeRankButton.innerText='Rank'


            // popup
            let popupElement = document.getElementById('popup');
            popupElement.innerHTML = '';

            buttonDiv.addEventListener('click', function () {
                $.ajax({
                    url: postUrl,  // Replace with your API endpoint URL
                    type: "POST",
                    data: {
                        // flag와 quizId
                        flag: flagDiv.value
                    },
                    success: function (response) {
                          // 성공적으로 응답 받은 경우, 콘솔에 출력합니다.
                        if (response == "Wrong") {
                            alert("틀렸습니다");
                            window.location.href = "/challenge";
                        } else if (response == "Correct") {
                            alert("맞았습니다");
                            window.location.href = "/challenge";
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        if (jqXHR.responseText == "ValidationError") {
                            alert("Validation Error")
                        } else if (jqXHR.responseText == "ctfFinish") {
                            alert("대회가 종료됬습니다")
                            window.location.href = "/";
                        } else if (jqXHR.responseText == "emptyFlag") {
                            alert("Flag 값이 비어있습니다")
                            window.location.href = "/challenge";
                        } else if (jqXHR.responseText="TooManyRequest"){
                            console.log(jqXHR)
                            alert("1분에 5번 이상 입력 금지입니다")
                        }
                        else {
                            alert("에러가 발생했습니다")
                        }
                    }
                });
            });
            if (quiz.attachment != null){
                let fileName = quiz.attachment.split('/').pop();
                attatchmentDiv.innerText = fileName;
                attatchmentDiv.href = "/quiz/download/" + id
                attatchmentDiv.className = 'popupBoxContent';
                titleAttatchmentDiv.className = 'popupBoxTitle';
            }


            // Append new div elements to the popup
            popupElement.appendChild(challengeContentDiv);
            popupElement.appendChild(challengeContentButton)
            popupElement.appendChild(challengeRankDiv);
            popupElement.appendChild(challengeRankButton)
            contentDiv.appendChild(nameDiv);
            contentDiv.appendChild(scoreDiv);
            titleFlexDiv.appendChild(titleWriterDiv);
            titleFlexDiv.appendChild(writerDiv);
            contentDiv.appendChild(titleFlexDiv);
            contentDiv.appendChild(titleDescriptionDiv);
            contentDiv.appendChild(descriptionDiv);
            if (quiz.attachment != null) {
                // popupElement.appendChild(titleAttatchmentDiv);
                contentDiv.appendChild(attatchmentDiv);
            }
            flagFlexDiv.appendChild(flagDiv);
            flagFlexDiv.appendChild(buttonDiv);
            contentDiv.appendChild(flagFlexDiv);
            popupElement.appendChild(contentDiv);
            popupElement.appendChild(rankTableDiv);

            // Show the dimmed background and popup
            document.getElementById('dimmed-bg').style.display = 'block';

            document.getElementById('rankTableDiv').style.display='none';
            // Show the dimmed background and popup
            document.getElementById('dimmed-bg').style.display = 'block';
            document.getElementById('popup').style.display = 'block';
        }
    }
    catch(error) {
        alert(error);
    };

}

document.getElementById('dimmed-bg').addEventListener('click', function() {
    this.style.display = 'none';
    document.getElementById('popup').style.display = 'none';
});

function getData(url) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,  // Replace with your API endpoint URL
            type: "GET",
            dataType: "json",
            success: function(response) {
                resolve(response);
            },
            error: function(error) {
                reject(error)
            },
        });
    });
}

