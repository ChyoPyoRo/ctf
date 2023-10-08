package kimdaehan.ctf.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(columnDefinition = "VARCHAR(64) NOT NULL")
    private String quizName;

}
