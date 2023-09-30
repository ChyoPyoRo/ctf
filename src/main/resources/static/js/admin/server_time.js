
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
            .then(response => console.log(response));

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
            .then(response => console.log(response));
    }
}