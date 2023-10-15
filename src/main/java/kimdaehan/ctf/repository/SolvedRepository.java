package kimdaehan.ctf.repository;

import kimdaehan.ctf.dto.UserPageDTO;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.SolvedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolvedRepository extends JpaRepository<Solved, SolvedId> {

    @Transactional(readOnly= true)
    List<Solved> findAllBySolvedIdSolvedUserId(String userId);

    @Transactional(readOnly= true)
    List<Solved> findAllBySolved(Quiz quiz);


    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time;", nativeQuery = true)
    List<UserPageDTO> findScoreUsers();

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.user_id = :userId " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time;", nativeQuery = true)
    Optional<UserPageDTO> findScoreUsersByUserId(@Param("userId") String userId);


}
