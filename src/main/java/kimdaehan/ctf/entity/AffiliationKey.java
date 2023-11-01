package kimdaehan.ctf.entity;

import jakarta.persistence.*;
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
public class AffiliationKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @EqualsAndHashCode.Include
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User userId;

    @EqualsAndHashCode.Include
    @Column(name = "registration_date_time")
    private LocalDateTime dateTime;


    @Column(columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private User.Affiliation rankAffiliation;
}