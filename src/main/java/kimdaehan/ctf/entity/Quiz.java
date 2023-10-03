package kimdaehan.ctf.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Quiz {
    @Id
    @Column(name="quiz_id", columnDefinition = "VARBINARY(64) NOT NULL")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID quizId;

    @Column(name="quiz_name", columnDefinition = "VARCHAR(64) NOT NULL")
    private String quizName;

    @Column(name="quiz_writer", columnDefinition = "VARCHAR(64) NOT NULL")
    private String quizWriter;

    @Column(name="description", columnDefinition = "VARCHAR(256) NOT NULL")
    private String description;

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT" )
    @Builder.Default
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime endTime;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer maxScore;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer minScore;

    @Column(columnDefinition = "VARCHAR(64) NOT NULL")
    private String attachment;

    @Column(columnDefinition = "VARBINARY(64) NOT NULL")
    private String flag;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer allUser;

    @Column(columnDefinition = "TINYINT(1) NOT NULL")
    private Integer delete;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer level;

    public enum CategoryType{
        REVERSING, PWN, WEB, FORENSICS, MISC, CRYPTO
    }
}
