


function showPopup(id) {
    var index = parseInt(id.substring(3));  // Remove 'row'
    var quiz = quizzes[index];

    // Fill the popup with data from 'quiz'
    document.getElementById('popup').querySelector('h5').innerText = quiz.quizName;
    document.getElementById('popup').querySelector('.chbox div').innerText = quiz.quizWriter;

    // Show the dimmed background and popup
    document.getElementById('dimmed-bg').style.display = 'block';
    document.getElementById('popup').style.display = 'block';
}
document.getElementById('dimmed-bg').addEventListener('click', function() {
    this.style.display = 'none';
    document.getElementById('popup').style.display = 'none';
});
<div id="popup"
     style="display:none;width:auto;height:auto;top:50%;left:50%;transform:translate(-50%, -50%);background-color:white;position:fixed;">
    <h5 th:text="${quiz.quizName}">Challenge Name</h5>
    <div className="form-floating mb-3 chbox">
        <div th:text="${quiz.quizWriter}"></div>
    </div>
    <div className="form-floating mb-3 chbox">
        <td th:if="${quiz.level == 0}">Low</td>
        <td th:if="${quiz.level == 1}">Middle</td>
        <td th:if="${quiz.level == 2}">High</td>
    </div>
