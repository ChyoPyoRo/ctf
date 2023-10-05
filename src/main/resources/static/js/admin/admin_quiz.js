function postQuiz() {

    const url = window.location.pathname;
    const file = document.getElementById("file").files[0];

    const formData = new FormData();

    formData.append('quizId', null);
    formData.append('quizName', document.getElementById("quizName").value);
    formData.append('category', document.getElementById("category").value);
    formData.append('level', document.getElementById("level").value);
    formData.append('description', document.getElementById("description").value);
    formData.append('flag', document.getElementById("flag").value);
    formData.append('flag', document.getElementById("startDate").value);
    formData.append('flag', document.getElementById("startTime").value);
    formData.append('file', file);

    const options = {
        method: "POST",
        cache: 'no-cache',
        body: formData,
    };

    fetch(url, options)
        .then(response => console.log(response));
}
