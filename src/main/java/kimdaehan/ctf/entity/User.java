package kimdaehan.ctf.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class User implements Serializable, UserDetails {
    @Id
    @Column(name="user_id", columnDefinition = "VARBINARY(64) NOT NULL",length=64, unique = true)
    @EqualsAndHashCode.Include
    private String userId;

    @Column(columnDefinition = "VARCHAR(64) NOT NULL" )
    private String password;

    @Column(columnDefinition = "VARCHAR(32) NOT NULL" )
    private String name;

    @Column(columnDefinition = "VARBINARY(32) NOT NULL" )
    private String nickName;

    @Column(columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" )
    @Builder.Default
    private LocalDateTime registrationDateTime = LocalDateTime.now();

    @Column(columnDefinition = "TIMESTAMP" )
    private LocalDateTime currentSolvedDateTime;

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "VARCHAR(8) NOT NULL", length = 8)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    @Enumerated(EnumType.STRING)
    private Affiliation affiliation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Type{
        USER, ADMIN;

    }
    public enum Affiliation{
        NB, YB, OB, SCH

    }

}
