

async function getScoreList(affiliation, scoreList){
    let rankData = await getRankAll(affiliation)
    while (scoreList.children.length>1){
        scoreList.removeChild(scoreList.lastChild)
    }
    rankData.forEach(function(item, index){
        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);

        var lineElement = createElementAndAppend('tr', '', 'scoreElement', scoreList);
        createElementAndAppend('td', index + 1, 'scoreRank', lineElement);
        createElementAndAppend('td', item.nickName, 'scoreNickName', lineElement);
        createElementAndAppend('td', item.score, 'scoreScore', lineElement);
        createElementAndAppend('td', item.solvedCount, 'scoreSolvedCount', lineElement);
        createElementAndAppend('td', item.solvedTime, 'scoreSolvedTime', lineElement);
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

function createElementAndAppend(tag, text, className, parent) {
    var element = document.createElement(tag);
    element.textContent = text;
    element.className = className;
    parent.appendChild(element);
    return element;
}