function updateClock() {
    const now = new Date();
    const end = new Date('2023-11-12T00:00:00');
    const last = end - now;
    const millisecondsInOneSecond = 1000;
    const millisecondsInOneMinute = millisecondsInOneSecond * 60;
    const millisecondsInOneHour = millisecondsInOneMinute * 60;
    const millisecondsInOneDay = millisecondsInOneHour * 24;

    const day = Math.floor(last / millisecondsInOneDay);
    const hours = Math.floor((last % millisecondsInOneDay) / millisecondsInOneHour);
    const minutes = Math.floor((last % millisecondsInOneHour) / millisecondsInOneMinute);
    const seconds = Math.floor((last % millisecondsInOneMinute) / millisecondsInOneSecond);

    const timeString = day +"일 "+ hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

    document.getElementById("main-time-clock").innerHTML = timeString;
}

// 1초마다 시간 업데이트
setInterval(updateClock, 1000);

// 페이지 로드 시 초기 시간 업데이트
updateClock();
