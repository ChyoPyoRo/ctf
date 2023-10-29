package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, RecordKey> {

    @Transactional(readOnly= true)
    Optional<AccessLog> findByRecordKey(RecordKey recordKey);

    // 찾기기능
    @Transactional(readOnly= true)
    List<AccessLog> findAllByOrderByRecordKeyDateTimeDesc();

    @Transactional(readOnly= true)
    List<AccessLog> findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(User recordKey_userId);

    @Transactional(readOnly= true)
    List<AccessLog> findAllByUserIpOrderByRecordKeyDateTimeDesc(String userIp);

    @Transactional(readOnly= true)
    List<AccessLog> findAllByQuizIdOrderByRecordKeyDateTimeDesc(Quiz quiz);


    @Transactional
    void deleteByQuizId(Quiz quiz);
    @Transactional
    void deleteByRecordKeyUserId(User user);

    @Modifying
    @Query("DELETE FROM AccessLog e WHERE e.recordKey.dateTime <= :dateTime")
    void deleteByRecordKeyDateTimeBefore(LocalDateTime dateTime);
}
