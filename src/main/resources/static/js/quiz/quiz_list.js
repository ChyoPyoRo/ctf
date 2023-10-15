async function showPopup(id) {
    try {
        // Get the clicked element
        const url = "/quiz/" + id
        const quiz = await getQuizData(url)
        const postUrl = "/challenge/" + id
        // Create new div elements for quizName and quizWriter
        let nameDiv = document.createElement('div');
        let writerDiv = document.createElement('div');
        let levelDiv = document.createElement('div');
        let descriptionDiv = document.createElement('div');
        let flagDiv = document.createElement("input");
        let buttonDiv =document.createElement("button");


        // Fill the divs with data from 'quiz'
        nameDiv.innerText = quiz.quizName;
        writerDiv.innerText = quiz.quizWriter.name;

        // Get the popup element and clear its current content
        let popupElement = document.getElementById('popup');
        popupElement.innerHTML = '';

        buttonDiv.addEventListener('click', function() {
            $.ajax({
                url: postUrl,  // Replace with your API endpoint URL
                type: "POST",
                data: {
                    // flag와 quizId
                    quizId : id,
                    flag: flagDiv.value
                },
                success: function(response) {
                    console.log(response);  // 성공적으로 응답 받은 경우, 콘솔에 출력합니다.
                    if(response == "Wrong"){
                        alert("틀렸습니다");
                        window.location.href = "/challenge";
                    }
                    else if(response=="Correct"){
                        alert("맞았습니다");
                        window.location.href = "/challenge";
                    }

                },
                error: function(error) {
                    alert(error);
                }
            });
        });


        // Append new div elements to the popup
        popupElement.appendChild(nameDiv);
        popupElement.appendChild(writerDiv);
        popupElement.appendChild(flagDiv);
        popupElement.appendChild(buttonDiv);

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
