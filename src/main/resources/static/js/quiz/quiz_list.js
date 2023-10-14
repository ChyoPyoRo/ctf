async function showPopup(id) {
    try {
        // Get the clicked element
        const url = "/quiz/" + id
        const quiz = await getQuizData(url)

        // Create new div elements for quizName and quizWriter
        let nameDiv = document.createElement('div');
        let writerDiv = document.createElement('div');
        let levelDiv = document.createElement('div');
        let descriptionDiv = document.createElement('div');
        let flagDiv = document.createElement("input");


        // Fill the divs with data from 'quiz'
        nameDiv.innerText = quiz.quizName;
        writerDiv.innerText = quiz.quizWriter.name;

        // Get the popup element and clear its current content
        let popupElement = document.getElementById('popup');
        popupElement.innerHTML = '';

        // Append new div elements to the popup
        popupElement.appendChild(nameDiv);
        popupElement.appendChild(writerDiv);

        // Show the dimmed background and popup
        document.getElementById('dimmed-bg').style.display = 'block';

        // Show the dimmed background and popup
        document.getElementById('dimmed-bg').style.display = 'block';
        document.getElementById('popup').style.display = 'block';
    }
    catch(error) {
        alert(error);
    };

}

document.getElementById('dimmed-bg').addEventListener('click', function() {
    this.style.display = 'none';
    document.getElementById('popup').style.display = 'none';
});

function getQuizData(url) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: url,  // Replace with your API endpoint URL
            type: "GET",
            dataType: "json",
            success: function(response) {
                resolve(response);
            },
            error: function(error) {
                reject(error);
            }
        });
    });
}
