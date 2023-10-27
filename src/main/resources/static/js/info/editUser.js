

function editUser(){
    const id = document.querySelector("input[id=id]").value;
    const password = document.querySelector("input[id=password]").value;
    const name = document.querySelector("input[id=name]").value;
    const nickName = document.querySelector("input[id=nickName]").value;
    const body = {
        userId: id,
        password: password,
        name: name,
        nickName : nickName
    };

    const url = "/editUser";
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
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data === 'OK') {
                    window.location.href = "/logout";
                    alert("정상적으로 변경 되었습니다.");
                } if(data === "NICK_NAME_EXIST"){
                    alert("이미 존재하는 닉네임 입니다.");
                }else {
                    alert("회원 가입 실패");
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