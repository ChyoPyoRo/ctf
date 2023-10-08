package kimdaehan.ctf.entity.log;

import jakarta.persistence.*;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import lombok.*;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AccessLog {
    @EmbeddedId
    RecordKey recordKey;

    @Column(columnDefinition = "VARCHAR(32) NOT NULL")
    private String userIp;

    @ManyToOne
    @JoinColumn(name="quiz_id", referencedColumnName = "quiz_id")
    private Quiz quizId;

}
