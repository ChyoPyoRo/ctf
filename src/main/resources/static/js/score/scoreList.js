

async function getScoreList(affiliation, scoreList){
    let rankData = await getRankAll(affiliation)
    while (scoreList.children.length>1){
        scoreList.removeChild(scoreList.lastChild)
    }
    rankData.forEach(function(item, index){
        var lineElement = document.createElement('tr');
        lineElement.className="scoreElement"
        var rankElement = document.createElement('td');
        rankElement.textContent = index + 1;
        rankElement.className="scoreRank"

        var nickNameElement = document.createElement('td');
        nickNameElement.textContent = item.nickName;
        nickNameElement.className="scoreNickName"

        var scoreElement = document.createElement('td');
        scoreElement.textContent = item.score;
        scoreElement.className="scoreScore"

        var solvedCountElement = document.createElement("td")
        solvedCountElement.textContent=item.solvedCount;
        solvedCountElement.className="scoreSolvedCount"

        var solvedTimeElement = document.createElement('td')
        solvedTimeElement.textContent=item.solvedTime;
        solvedTimeElement.className="scoreSolvedTime"

        lineElement.appendChild(rankElement)
        lineElement.appendChild(nickNameElement)
        lineElement.appendChild(scoreElement)
        lineElement.appendChild(solvedCountElement)
        lineElement.appendChild(solvedTimeElement)

        scoreList.appendChild(lineElement);
    })
}

async function getRankAll(affilation){
    let url = "/rank-all/" + affilation;
    try{
        return await getAjax(url)
    } catch(e){
        console.log(e)
    }
}