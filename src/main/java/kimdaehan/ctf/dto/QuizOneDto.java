package kimdaehan.ctf.dto;

import kimdaehan.ctf.entity.Quiz;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizOneDto {
    private String quizName;
    private String author;
    private String description;
    private Integer score;
    private LocalDateTime startTime;

    private  QuizOneDto(Quiz quiz){
        this.quizName = quiz.getQuizName();
        this.author = quiz.getQuizWriter().getNickName();
        this.description = quiz.getDescription();
        this.score = quiz.getScore();
        this.startTime = quiz.getStartTime();
    }

    public static QuizOneDto from(Quiz quiz){
        return new QuizOneDto(quiz);
    }
}
