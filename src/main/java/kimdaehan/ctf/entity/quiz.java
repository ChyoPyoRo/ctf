package kimdaehan.ctf.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class quiz {
    @Id
    @Column(name="quiz_id", columnDefinition = "INT NOT NULL")
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

    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" )
    @Builder.Default
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP NOT NULL CURRENT_TIMESTAMP")
    private LocalDateTime endTime;

    @Column(columnDefinition = "NUMBER NOT NULL")
    private Number maxScore;

    @Column(columnDefinition = "NUMBER NOT NULL")
    private Number minScore;

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
