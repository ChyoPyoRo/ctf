package kimdaehan.ctf.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kimdaehan.ctf.entity.Quiz;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String quizId;
    private String quizName;
    private String description;
    private String category;
    private LocalDate startDate;
    private LocalTime startTime;
    private String flag;
    private String level;
    private MultipartFile file;

    public Quiz dtoToQuiz(){
        return Quiz.builder()
                .quizName(this.quizName)
                .description(this.description)
                .category(Quiz.CategoryType.valueOf(this.category))
                .level(Integer.valueOf(this.level))
                .startTime(this.startDate.atTime(this.startTime))
                .flag(this.flag)
                .build();
    }
    public LocalDateTime getLocalDateTime(){
        return this.startDate.atTime(this.startTime);
    }
}
