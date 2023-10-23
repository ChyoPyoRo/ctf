package kimdaehan.ctf.repository;

import kimdaehan.ctf.dto.RankGraphDTO;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.entity.RecordKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
