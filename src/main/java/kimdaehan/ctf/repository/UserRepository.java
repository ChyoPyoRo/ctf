package kimdaehan.ctf.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import kimdaehan.ctf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Transactional(readOnly= true)
    Optional<User> findByUserId(String userId);

    @Transactional(readOnly = true)
    List<User> findAllByTypeAndAffiliationAndIsBan(User.Type type, User.Affiliation affiliation, User.IsBan isBan);

    @Transactional(readOnly = true)
    Optional<User> findByNickName(String nickName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.currentSolvedDateTime = null")
    void updateCurrentSolvedDateTime();

}
