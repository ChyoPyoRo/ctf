
function postServerTime(type){
    if(type === "START"){
        const body = {
            "type" : type,
            "serverDate" : document.getElementById("start-date").value,
            "serverTime" : document.getElementById("start-time").value
        }
        const url = "/server_setting";
        const options = {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        };
        fetch(url, options)
            .then(response => response.text())
            .then(data => {
                // 문자열 데이터(data)를 사용하여 원하는 작업 수행
                if(data === "success"){
                    alert("성공적으로 변경되었습니다.");
                } else{
                    alert("에러가 발생하였습니다.")
                }
                window.location.reload();
            });

    } else {
        const body = {
            "type" : type,
            "serverDate" : document.getElementById("end-date").value,
            "serverTime" : document.getElementById("end-time").value
        }
        const url = "/server_setting";
        const options = {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        };
        fetch(url, options)
            .then(response => response.text())
            .then(data => {
            // 문자열 데이터(data)를 사용하여 원하는 작업 수행
                if(data === "success"){
                    alert("성공적으로 변경되었습니다.");
                } else{
                    alert("에러가 발생하였습니다.")
                }
                window.location.reload();
            });
    }
}