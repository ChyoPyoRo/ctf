package kimdaehan.ctf.entity;

import jakarta.persistence.*;
import kimdaehan.ctf.entity.User;
import lombok.*;

import java.io.Serializable;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Solved implements Serializable {
    //문제 ID
    @EmbeddedId
    private SolvedId solvedId;

    //문제 푼 사람 어드민 여부
    @Column(name="solved_user_type", columnDefinition = "VARCAHR(8) NOT NULL", length = 8)
    @Enumerated(EnumType.STRING)
    private User.Type solvedUserType;
    //문제 푼 사람 소속
    @Column(name="solved_user_affliation", columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private User.Affiliation solvedUserAffiliation;


}
