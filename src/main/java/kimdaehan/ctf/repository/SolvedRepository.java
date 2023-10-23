package kimdaehan.ctf.repository;

import kimdaehan.ctf.dto.RankGraphCurrentDTO;
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


    //유저의 소속, id별 정보(score 포함)
    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time " +
            "ORDER BY u.registration_date_time DESC;", nativeQuery = true)
    List<UserPageDTO> findScoreUsers();

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.user_id = :userId " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time " +
            "ORDER BY u.registration_date_time DESC;", nativeQuery = true)
    Optional<UserPageDTO> findScoreUsersByUserId(@Param("userId") String userId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.affiliation = :affiliation " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time " +
            "ORDER BY u.registration_date_time DESC;", nativeQuery = true)
    List<UserPageDTO> findScoreUsersByAffiliation(@Param("affiliation") String affiliation);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.affiliation = :affiliation and u.user_id = :userId " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time " +
            "ORDER BY u.registration_date_time DESC;", nativeQuery = true)
    Optional<UserPageDTO> findScoreUsersByAffiliationAndUserId(@Param("affiliation") String affiliation, @Param("userId") String userId);

    // Rank 사용할 거
    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as user_id, u.name as name, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nick_name, u.registration_date_time as registration_date_time, u.is_ban as is_ban, " +
            "       COALESCE(SUM(q.score), 0) AS total_score " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.affiliation = :affiliation and u.is_ban = 'DISABLE' and u.type = 'USER' " +
            "GROUP BY u.user_id, u.name, u.affiliation, u.nick_name, u.registration_date_time " +
            "ORDER BY total_score DESC;", nativeQuery = true)
    List<UserPageDTO> findRankAndScoreUsersByAffiliation(@Param("affiliation") String affiliation);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(u.user_id USING utf8) as userId, u.affiliation as affiliation, CONVERT(u.nick_name USING utf8) as nickName, " +
            "       COALESCE(SUM(q.score), 0) AS totalScore " +
            "FROM user u " +
            "LEFT JOIN solved s ON u.user_id = s.solved_user_id " +
            "LEFT JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE u.affiliation = :affiliation and u.is_ban = 'DISABLE' and u.type = 'USER' " +
            "GROUP BY u.user_id, u.name, u.affiliation " +
            "ORDER BY totalScore DESC LIMIT 5;", nativeQuery = true)
    List<RankGraphCurrentDTO> findRankAndScoreUsersByAffiliationTop5(@Param("affiliation") String affiliation);



}
