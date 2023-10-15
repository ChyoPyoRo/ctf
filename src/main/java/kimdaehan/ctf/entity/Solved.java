package kimdaehan.ctf.entity;

import jakarta.persistence.*;
import kimdaehan.ctf.entity.User;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Solved implements Serializable {
    //문제 ID
    @EmbeddedId
    private SolvedId solvedId;

    @MapsId("solvedQuizId")
    @ManyToOne
    private Quiz solved;

    @Column( columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime solvedTime = LocalDateTime.now();
}
