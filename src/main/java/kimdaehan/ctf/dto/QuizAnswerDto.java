package kimdaehan.ctf.dto;


import kimdaehan.ctf.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerDto {
    private User user;
    private String quizId;
}
