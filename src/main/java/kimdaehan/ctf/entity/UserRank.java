package kimdaehan.ctf.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserRank {
    @EmbeddedId
    RecordKey recordKey;

    @Column(columnDefinition = "INT NOT NULL")
    @Builder.Default
    private Integer score = 0;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer userRank;

    @Column(columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private User.Affiliation rankAffiliation;
}
