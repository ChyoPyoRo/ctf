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

function postQuiz(quizId) {

    const url = window.location.pathname;
    const file = document.getElementById("file").files[0];

    const formData = new FormData();

    if(quizId !== null){
        formData.append('quizId', quizId);
    }
    if(file !== undefined){
        formData.append('file', file);
    }
    formData.append('quizName', document.getElementById("quizName").value);
    formData.append('category', document.getElementById("category").value);
    formData.append('level', document.getElementById("level").value);
    formData.append('description', document.getElementById("description").value);
    formData.append('flag', document.getElementById("flag").value);
    formData.append('startDate', document.getElementById("startDate").value);
    formData.append('startTime', document.getElementById("startTime").value);
    const options = {
        method: "POST",
        cache: 'no-cache',
        body: formData,
    };

    fetch(url, options)
        .then(response => console.log(response));
}

async function getQuizList(category){
    const url = "/admin_quiz_list/"+category;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}



async function renderPagination(currentPage) {
    const contents = document.querySelector("#quiz-content");
    const data = await getQuizList("ALL");
    let totalCount = data.length;
    let maxPage = Math.ceil(totalCount / 8);

    let maxGroup = Math.ceil(maxPage / 5);
    let currentGroup = Math.ceil(currentPage / 5)
    let currentData;
    if((currentPage-1) * 8  >= totalCount){
        currentData = data.slice((currentPage-1) * 8, totalCount);
    }else {
        currentData = data.slice((currentPage-1) * 8, (currentPage) * 8);
    }
    if( currentGroup === maxGroup ){
        renderButton((currentGroup * 5)-4,maxPage, currentPage -((currentGroup * 5)-4));
        renderTable(currentData);
    } else {
        renderButton((currentGroup * 5)-4, currentGroup * 5, currentPage -((currentGroup * 5)-4))
        renderTable(currentData);
    }

}


const renderButton = (page, maxPage, now) => {
    const buttons = document.querySelector("#paging");
    // 버튼 리스트 초기화
    while (buttons.hasChildNodes()) {
        buttons.removeChild(buttons.lastChild);
    }
    // 화면에 최대 5개의 페이지 버튼 생성
    for (let id = page; id <= maxPage; id++) {
        buttons.appendChild(makeButton(id));
    }
    // 첫 버튼 활성화(class="active")
    buttons.children[now].classList.add("active");

    const prev = document.createElement("button");
    prev.classList.add("button", "prev");
    prev.innerHTML = '<ion-icon name="chevron-back-outline"></ion-icon>';
    //prev.addEventListener("click", goPrevPage);

    const next = document.createElement("button");
    next.classList.add("button", "next");
    next.innerHTML = '<ion-icon name="chevron-forward-outline"></ion-icon>';
    //next.addEventListener("click", goNextPage);


    buttons.prepend(prev);
    buttons.append(next);

    // 이전, 다음 페이지 버튼이 필요한지 체크
   if (page - maxPage < 1) buttons.removeChild(prev);
   if (page + 1 > maxPage) buttons.removeChild(next);
};


const renderTable = (dataSet) => {
    const contents = document.querySelector("#quiz-content");
    for(let i=0; i<dataSet.length; i++){
        contents.appendChild(makeTable(dataSet[i]));
    }
}

const makeButton = (id) => {
    const buttons = document.querySelector("#paging");
    const button = document.createElement("button");
    button.classList.add("button");
    button.dataset.num = id;
    button.innerText = id;
    button.addEventListener("click", (e) => {
        Array.prototype.forEach.call(buttons.children, (button) => {
            if (button.dataset.num) button.classList.remove("active");
        });
        e.target.classList.add("active");
        //renderContent(parseInt(e.target.dataset.num));
    });
    return button;
};

const makeTable = (data) => {
    const table = document.createElement("tr");
    const td1 = document.createElement("td");
    const aTag= document.createElement('a');
    aTag.href="/admin_quiz/edit?uuid="+data.quizId;
    aTag.innerText = data.quizName;
    td1.appendChild(aTag);

    const td2 = document.createElement("td");
    td2.innerText = data.quizWriter;
    const td3 = document.createElement("td");
    td3.innerText = data.category;
    const td4 = document.createElement("td");
    if(data.level === 0){
        td4.innerText = "LOW";
    } else if(data.level === 1){
        td4.innerText = "MIDDLE";
    } else {
        td4.innerText = "HIGH";
    }
    const td5 = document.createElement("td");
    td5.innerText = data.registrationTime;
    const td6 = document.createElement("td");
    td6.innerText = data.startTime;

    const td7=document.createElement('td');
    const aTag2= document.createElement('a');
    const button = document.createElement("button");
    aTag2.href="/admin_quiz/delete?uuid="+data.quizId;
    aTag2.innerText = "delete";
    button.appendChild(aTag2);
    td7.appendChild(button);

    table.appendChild(td1);
    table.appendChild(td2);
    table.appendChild(td3);
    table.appendChild(td4);
    table.appendChild(td5);
    table.appendChild(td6);
    table.appendChild(td7);

    return table;
};
