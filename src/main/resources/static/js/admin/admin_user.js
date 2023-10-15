

function edit(){
    const id = document.querySelector("input[id=userId]").value;
    const password = document.querySelector("input[id=userPassword]").value;
    const name = document.querySelector("input[id=userName]").value;
    const affiliation = document.querySelector("select[id=affiliation]").value;
    const nickName = document.querySelector("input[id=userNickName]").value;
    const type = document.querySelector("select[id=type]").value;
    const body = {
        userId: id,
        password: password,
        name: name,
        nickName : nickName,
        affiliation : affiliation,
        type: type
    };

    const url = window.location.pathname;
    const options = {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    };

    if(isEmpty(removeSpace(id))){
        alert("아이디가 공백입니다.");
    }
    else {
        fetch(url, options)
            .then(response => {return response})
            .then(data => {
                console.log(data);
                if (data.ok === true) {
                    alert("정상적으로 회원 정보가 수정 되었습니다.");
                    window.location.href = "/admin_user";
                }  else {
                    alert("회원 정보 수정에 실패 했습니다.")
                }
            });
    }
}

function isEmpty(value){
    return value.length === 0;
}
function removeSpace(value) {
    return value.replace(/\s/g,"");
}



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

const getAjaxText = function(url) {
    return new Promise((resolve, reject) => { // 1.
        $.ajax({
            url: url,
            type: "GET",
            dataType: "text"
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

async function getUserList(type, id){
    let url = "/admin_user_list/"+type+'/'+id;
    try {
        return await getAjax(url);
    } catch(e) {
        console.log(e);
    }
}

async function userBan(id){
    let url = "/admin_user_ban/"+id;
    try {
        const data = await getAjaxText(url);
        console.log(data);
        window.location.reload();
    } catch(e) {
        console.log(e);
        alert("Ban Error");
    }
}

async function renderPagination(currentPage, type,id) {
    const data = await getUserList(type,id);

    let totalCount = data.length;
    if(data[0] === null){
        totalCount = 0;
    }
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
            await renderButton((currentGroup * 5)-4,maxPage, currentPage -((currentGroup * 5)-4),type, maxPage,id);
            renderTable(currentData);
        } else {
            await renderButton((currentGroup * 5)-4, currentGroup * 5, currentPage -((currentGroup * 5)-4),type, maxPage,id)
            renderTable(currentData);
        }
    }


}
const noDataRender = () =>{
    const tables = document.querySelector("#user-content");
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

const renderButton = async (page, maxPage, now,type, totalMaxPage,userId) => {
    const buttons = document.querySelector("#paging");
    // 버튼 리스트 초기화
    while (buttons.hasChildNodes()) {
        buttons.removeChild(buttons.lastChild);
    }
    // 화면에 최대 5개의 페이지 버튼 생성
    for (let id = page; id <= maxPage; id++) {
        buttons.appendChild(makeButton(id,type,userId));
    }
    // 첫 버튼 활성화(class="active")
    buttons.children[now].classList.add("active");

    const prev = document.createElement("button");
    prev.classList.add("btn", "btn-secondary");
    prev.innerHTML = '<i class="fa-solid fa-caret-left"></i>';
    prev.onclick = () => renderPagination(now,type,userId);

    const next = document.createElement("button");
    next.classList.add("btn", "btn-secondary");
    next.innerHTML = '<i class="fa-solid fa-caret-right"></i>';
    next.onclick = () => renderPagination(now+2,type,userId);

    buttons.prepend(prev);
    buttons.append(next);
    console.log(page, maxPage)
    // 이전, 다음 페이지 버튼이 필요한지 체크
    if ((now+1)  === 1 ) buttons.removeChild(prev);
    if ((now+1) === totalMaxPage) buttons.removeChild(next);
};


const renderTable = (dataSet) => {
    const tables = document.querySelector("#user-content");
    // 버튼 리스트 초기화
    while (tables.hasChildNodes()) {
        tables.removeChild(tables.lastChild);
    }

    for(let i=0; i<dataSet.length; i++){
        tables.appendChild(makeTable(dataSet[i]));
    }
}

const makeButton = (id,type,userId) => {
    const buttons = document.querySelector("#paging");
    const button = document.createElement("button");
    button.classList.add("btn","btn-secondary");
    button.dataset.num = id;
    button.innerText = id;
    button.onclick = () => renderPagination(id,type,userId);
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
    aTag.href="/admin_user/detail/"+data.user_id;
    aTag.innerText = data.user_id;
    td1.appendChild(aTag);

    const td2 = document.createElement("td");
    td2.innerText = data.name;
    const td3 = document.createElement("td");
    td3.innerText = data.nick_name;
    const td4 = document.createElement("td");
    td4.innerText = data.total_score;
    const td5 = document.createElement("td");
    td5.innerText = data.affiliation;
    const td6 = document.createElement("td");
    td6.innerText = data.registration_date_time;

    const td7=document.createElement('td');
    const button = document.createElement("button");
    button.style.color="white";
    button.onclick = () => userBan(data.user_id);
    if(data.is_ban === "DISABLE"){
        button.classList.add("btn-danger","btn");
        button.innerText = "Disable";

    } else {
        button.classList.add("btn-success","btn");
        button.innerText = "Enable";
    }

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