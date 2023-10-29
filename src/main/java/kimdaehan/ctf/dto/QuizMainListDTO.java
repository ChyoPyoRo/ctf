package kimdaehan.ctf.dto;

import kimdaehan.ctf.entity.Quiz;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
public class QuizMainListDTO {
    private UUID quizIdAsUuid;
    private String quizName;
    private String author;
    private Quiz.CategoryType category;
    private Integer score;
    private String test;
    private Boolean isSolved;

    private QuizMainListDTO(QuizListDTO quiz, Boolean isSolved){
        this.quizIdAsUuid = quiz.getQuizIdAsUuid();
        this.test = quiz.getTest();
        this.quizName = quiz.getQuizName();
        this.author = quiz.getAuthor();
        this.category = quiz.getCategoryByEnum();
        this.score = quiz.getScore();
        this.isSolved = isSolved;
    }
    public static QuizMainListDTO from(QuizListDTO quiz, Boolean isSolved){
        return new QuizMainListDTO(quiz, isSolved );
    }
    public boolean isSolved(){
        return isSolved;
    }
}
