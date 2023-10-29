package kimdaehan.ctf.repository;

import kimdaehan.ctf.dto.QuizRankDto;
import kimdaehan.ctf.dto.RankGraphDTO;
import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.entity.RecordKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RankRepository extends JpaRepository<UserRank, RecordKey> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT CONVERT(user_id USING utf8) as userId, user_rank as userRank, score as score, registration_date_time as dateTime " +
            "FROM user_rank " +
            "WHERE rank_affiliation = :affiliation " +
            "ORDER BY dateTime ASC, userRank ASC;",nativeQuery = true)
    List<RankGraphDTO> findRankGraphByAffiliation(@Param("affiliation") String affiliation);

    @Query(value = "SELECT CONVERT(user_id USING utf8) as userId, user_rank as userRank, score as score, registration_date_time as dateTime " +
            "FROM user_rank " +
            "WHERE user_id = :userId " +
            "ORDER BY dateTime DESC;",nativeQuery = true)
    List<RankGraphDTO> findRankGraphByUserId(@Param("userId") String userId);

    @Transactional(readOnly = true)
    @Query(value="SELECT s.solved_time as solvedTime, CONVERT(u.nick_name USING utf8) as nickName " +
            " FROM solved s " +
            " LEFT JOIN user u on (s.solved_user_id = u.user_id) " +
            " WHERE s.solved_quiz_id = :challengeId ", nativeQuery = true)
    List<QuizRankDto> findRankByChallengeId(@Param("challengeId") UUID challengeId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserRank e WHERE e.recordKey.dateTime <= :dateTime")
    void deleteByRecordKeyDateTimeBefore(LocalDateTime dateTime);

}
