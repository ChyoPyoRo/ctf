

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