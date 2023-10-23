package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlagLogRepository extends JpaRepository<FlagLog, RecordKey> {
    @Transactional(readOnly= true)
    Optional<FlagLog> findByRecordKey(RecordKey recordKey);

    // 찾기
    @Transactional(readOnly= true)
    List<FlagLog> findAllByOrderByRecordKeyDateTimeDesc();
    @Transactional(readOnly= true)
    List<FlagLog> findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(User recordKey_userId);

    @Transactional(readOnly= true)
    List<FlagLog> findAllByUserIpOrderByRecordKeyDateTimeDesc(String userIp);

    @Transactional(readOnly= true)
    List<FlagLog> findAllByQuizIdOrderByRecordKeyDateTimeDesc(Quiz quiz);
    @Transactional(readOnly= true)
    List<FlagLog> findAllBySuccessFailOrderByRecordKeyDateTimeDesc(FlagLog.SuccessOrNot successOrNot);

//    @Transactional(readOnly = true)
//    @Query(value="SELECT d FROM flaglog AS d WHERE"
//    )
//    List<FlagLog> find
    @Transactional(readOnly = true)
    @Query("SELECT f FROM FlagLog AS f WHERE f.recordKey.dateTime >= :oneMinuteAgo AND f.recordKey.userId = :userId AND f.quizId = :quizId ")
    List<FlagLog> findListBeforeOneMinute(@Param("quizId") String userId,@Param("quizId") UUID quizId, @Param("oneMinuteAgo") LocalDateTime oneMinuteAgo);


    @Transactional
    void deleteByQuizId(Quiz quiz);

    @Transactional
    void deleteByRecordKeyUserId(User user);
}
