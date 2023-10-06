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
