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


async function getLogList(category){
    const url = "/admin_quiz_list/"+category;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}



async function renderPagination(currentPage, type) {
    const contents = document.querySelector("#quiz-content");
    const data = await getQuizList(type);
    let totalCount = data.length;

    let maxPage = Math.ceil(totalCount / 10);

    let maxGroup = Math.ceil(maxPage / 5);
    let currentGroup = Math.ceil(currentPage / 5)
    let currentData;
    if((currentPage-1) * 10  >= totalCount){
        currentData = data.slice((currentPage-1) * 10, totalCount);
    }else {
        currentData = data.slice((currentPage-1) * 10, (currentPage) * 10);
    }
    if(totalCount === 0){
        noDataRender();
    } else{
        if( currentGroup === maxGroup ){
            await renderButton((currentGroup * 5)-4,maxPage, currentPage -((currentGroup * 5)-4),type, maxPage);
            renderTable(currentData);
        } else {
            await renderButton((currentGroup * 5)-4, currentGroup * 5, currentPage -((currentGroup * 5)-4),type, maxPage)
            renderTable(currentData);
        }
    }


}
const noDataRender = () =>{
    const tables = document.querySelector("#quiz-content");
    while (tables.hasChildNodes()) {
        tables.removeChild(tables.lastChild);
    }

    const buttons = document.querySelector("#paging");
    // 버튼 리스트 초기화
    while (buttons.hasChildNodes()) {
        buttons.removeChild(buttons.lastChild);
    }

    const table = document.createElement("tr");
    const td = document.createElement("td");
    td.innerText="No Input Data"
    td.colSpan=7;
    td.style.height="48.818182px";
    td.style.textAlign="center";
    table.appendChild(td);
    tables.appendChild(table);
}

const renderButton = async (page, maxPage, now,type, totalMaxPage) => {
    const buttons = document.querySelector("#paging");
    // 버튼 리스트 초기화
    while (buttons.hasChildNodes()) {
        buttons.removeChild(buttons.lastChild);
    }
    // 화면에 최대 5개의 페이지 버튼 생성
    for (let id = page; id <= maxPage; id++) {
        buttons.appendChild(makeButton(id,type));
    }
    // 첫 버튼 활성화(class="active")
    buttons.children[now].classList.add("active");

    const prev = document.createElement("button");
    prev.classList.add("btn", "btn-secondary");
    prev.innerHTML = '<i class="fa-solid fa-caret-left"></i>';
    prev.onclick = () => renderPagination(now,type);

    const next = document.createElement("button");
    next.classList.add("btn", "btn-secondary");
    next.innerHTML = '<i class="fa-solid fa-caret-right"></i>';
    next.onclick = () => renderPagination(now+2,type);

    buttons.prepend(prev);
    buttons.append(next);
    console.log(page, maxPage)
    // 이전, 다음 페이지 버튼이 필요한지 체크
    if ((now+1)  === 1 ) buttons.removeChild(prev);
    if ((now+1) === totalMaxPage) buttons.removeChild(next);
};


const renderTable = (dataSet) => {
    const tables = document.querySelector("#quiz-content");
    // 버튼 리스트 초기화
    while (tables.hasChildNodes()) {
        tables.removeChild(tables.lastChild);
    }

    for(let i=0; i<dataSet.length; i++){
        tables.appendChild(makeTable(dataSet[i]));
    }
}

const makeButton = (id,type) => {
    const buttons = document.querySelector("#paging");
    const button = document.createElement("button");
    button.classList.add("btn","btn-secondary");
    button.dataset.num = id;
    button.innerText = id;
    button.onclick = () => renderPagination(id,type);
    button.addEventListener("click", (e) => {
        Array.prototype.forEach.call(buttons.children, (button) => {
            if (button.dataset.num) button.classList.remove("active");
        });
        e.target.classList.add("active");
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
    const button = document.createElement("button");
    button.classList.add("btn-danger","btn");
    button.onclick= () =>{
        const options = {
            method: "GET",
        };
        fetch("/admin_quiz/delete/"+data.quizId, options)
            .then(response => {return response; })
            .then(data => {
                if(data.ok === true){
                    alert("문제 삭제에 성공 했습니다.");
                    window.location.reload();
                } else {
                    alert("문제 삭제에 실패 했습니다.");
                }
            });
    }

    button.style.color="white";
    button.innerText = "delete";
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
