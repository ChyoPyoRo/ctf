
function postServerTime(type){
    if(type === "START"){
        const body = {
            type : type,
            serverDate : document.getElementById("start-date"),
            serverTime : document.getElementById("start-time")
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
            .then(response => response.json())
            .then(data => {
                console.log(data);
            });

    } else {
        const body = {
            type : type,
            serverDate : document.getElementById("end-date"),
            serverTime : document.getElementById("end-time")
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
            .then(response => response.json())
            .then(data => {
                console.log(data);
            });
    }
}