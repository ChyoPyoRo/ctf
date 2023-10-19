async function showPopup(id) {
    try {
        const url = "/quiz/" + id
        const quiz = await getQuizData(url)
        const postUrl = "/challenge/" + id

        //Div Element 추가
        let titleNameDiv = document.createElement('h3');
        let nameDiv = document.createElement('div');
        let titleWriterDiv = document.createElement('h3');
        let writerDiv = document.createElement('div');
        let titleScoreDiv = document.createElement('h3');
        let scoreDiv = document.createElement('div');
        let titleDescriptionDiv = document.createElement('h3');
        let descriptionDiv = document.createElement('div');
        let titleFlag = document.createElement('h3');
        let flagDiv = document.createElement("input");
        let buttonDiv =document.createElement("button");

        //quiz정보 입력
        titleNameDiv.innerText = "문제이름"
        titleWriterDiv.innerText = "작성자"
        titleScoreDiv.innerText  = "점수"
        titleDescriptionDiv.innerText="설 명"
        titleFlag.innerText="Flag"

        nameDiv.className = 'popupBoxContent';
        writerDiv.className = 'popupBoxContent';
        scoreDiv.className = 'popupBoxContent';
        descriptionDiv.className = 'popupBoxContent description';

        titleNameDiv.className = 'popupBoxTitle';
        titleWriterDiv.className = 'popupBoxTitle';
        titleScoreDiv.className = 'popupBoxTitle';
        titleDescriptionDiv.className = 'popupBoxTitle'
        titleFlag.className='popupBoxTitle flag-title'

        // descriptionDiv.className="description"
        // titleFlag.className="flag-title";

        nameDiv.innerText = quiz.quizName;
        writerDiv.innerText = quiz.quizWriter.name;
        scoreDiv.innerText = quiz.score;
        descriptionDiv.innerText = quiz.description;
        buttonDiv.innerText = "제 출";

        // popup
        let popupElement = document.getElementById('popup');
        popupElement.innerHTML = '';

        buttonDiv.addEventListener('click', function() {
            $.ajax({
                url: postUrl,  // Replace with your API endpoint URL
                type: "POST",
                data: {
                    // flag와 quizId
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
                error: function(jqXHR, textStatus, errorThrown) {
                    if(jqXHR.responseText == "WrongID"){
                        alert("존재하지 않는 ID입니다")
                    }else if(jqXHR.responseText=="NotExistID"){
                        alert("존재하지 않는 문제입니다")
                    }else if(jqXHR.responseText=="ctfFinish"){
                        alert("대회가 종료됬습니다")
                        window.location.href="/";
                    }else if(jqXHR.responseText=="notOpen"){
                        alert("해당 문제는 아직 풀 수 없습니다")
                        window.location.href="/challenge";
                    }
                    else{
                        alert("에러가 발생했습니다")
                    }
                }
            });
        });
        if (quiz.attachment != null){
            //추가파일이 있으면
            let attatchmentDiv = document.createElement('a');
            let titleAttatchmentDiv = document.createElement("h3");

            attatchmentDiv.innerText="file 명";
            titleAttatchmentDiv.innerText = "파일";
            attatchmentDiv.href = quiz.attachment;

            attatchmentDiv.className = 'popupBoxContent';
            titleAttatchmentDiv.className = 'popupBoxTitle';
        }


        // Append new div elements to the popup
        popupElement.appendChild(titleNameDiv);
        popupElement.appendChild(nameDiv);
        popupElement.appendChild(titleWriterDiv);
        popupElement.appendChild(writerDiv);
        popupElement.appendChild(titleScoreDiv);
        popupElement.appendChild(scoreDiv);
        popupElement.appendChild(titleDescriptionDiv);
        popupElement.appendChild(descriptionDiv);
        if( quiz.attachment!=null){
            popupElement.appendChild(titleAttatchmentDiv);
            popupElement.appendChild(attatchmentDiv);
        }
        popupElement.appendChild(titleFlag);
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
