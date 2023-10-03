package kimdaehan.ctf.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kimdaehan.ctf.entity.Quiz;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

public class QuizDto {
    private UUID quizId;
    private String quizName;
    private String quizWriter;
    private String description;
    private Quiz.CategoryType category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxScore;
    private Integer minScore;
    private String attachment;
    private String flag;
    private Integer level;
}
