package kimdaehan.ctf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RecordKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @EqualsAndHashCode.Include
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User userId;

    @EqualsAndHashCode.Include
    @Column(name = "registration_date_time")
    private LocalDateTime dateTime;

}