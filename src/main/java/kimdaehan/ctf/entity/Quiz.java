package kimdaehan.ctf.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Quiz implements Serializable {
    @Id
    @Column(name="quiz_id", columnDefinition = "VARBINARY(64) NOT NULL")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID quizId = UUID.randomUUID();

    @Column(name="quiz_name", columnDefinition = "VARCHAR(64) NOT NULL")
    private String quizName;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "user_id")
    private User quizWriter;

    @Column(name="description", columnDefinition = "VARCHAR(1024)")
    private String description;

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registrationTime = LocalDateTime.now();

    @Column(columnDefinition = "INT NOT NULL Default 1000")
    @Builder.Default
    private Integer maxScore = 1000;

    @Column(columnDefinition = "INT NOT NULL Default 100")
    @Builder.Default
    private Integer minScore = 100;

    @Column(columnDefinition = "INT NOT NULL Default 1000")
    @Builder.Default
    private Integer score = 1000;

    @Column(columnDefinition = "VARCHAR(64)")
    private String attachment;

    @Column(columnDefinition = "VARBINARY(256) NOT NULL")
    private String flag;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer level;




    public enum CategoryType{
        REVERSING, PWN, WEB, FORENSICS, MISC
    }

    public enum levelType{
        LOW, MIDDLE, HIGH
    }
}
