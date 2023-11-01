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
    AffiliationKey affiliationKey;

    @Column(columnDefinition = "INT NOT NULL")
    @Builder.Default
    private Integer score = 0;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer userRank;


}
