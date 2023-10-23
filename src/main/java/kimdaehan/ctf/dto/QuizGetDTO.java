package kimdaehan.ctf.dto;

import kimdaehan.ctf.entity.Quiz;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
public class QuizGetDTO {
    private UUID quizId;
    private String quizName;
    private String author;
    private Quiz.CategoryType category;
    private Integer score;
    private String test;

    private QuizGetDTO(QuizListDTO quiz){
        this.quizId = quiz.getQuizIdAsUuid();
        this.test = quiz.getTest();
        this.quizName = quiz.getQuizName();
        this.author = quiz.getAuthor();
        this.category = quiz.getCategoryByEnum();
        this.score = quiz.getScore();
    }

    public static QuizGetDTO from(QuizListDTO quiz){
        return new QuizGetDTO(quiz);
    }
}
