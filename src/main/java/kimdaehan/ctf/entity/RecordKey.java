package kimdaehan.ctf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private String userId;

    @EqualsAndHashCode.Include
    @Column(name = "registration_date_time")
    private LocalDateTime dateTime;

}