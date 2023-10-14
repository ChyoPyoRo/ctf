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

    @MapsId("solvedQuizId")
    @ManyToOne
    private Quiz quiz;
}
