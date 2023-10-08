package kimdaehan.ctf.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
public class FlagLog {
    @EmbeddedId
    RecordKey recordKey;

    @Column(columnDefinition = "VARCHAR(32) NOT NULL")
    private String userIp;

    @Column(columnDefinition = "VARCHAR(64) NOT NULL")
    private String quizName;

    @Column(columnDefinition = "VARBINARY(64) NOT NULL")
    private String flag;

    @Column(columnDefinition = "VARBINARY(64) NOT NULL")
    private SuccessOrNot successOrNot;


    public enum SuccessOrNot{
        SUCCESS, FAIL
    }
}
