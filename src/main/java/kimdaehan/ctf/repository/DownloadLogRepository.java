package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog, RecordKey> {
    @Transactional(readOnly= true)
    Optional<DownloadLog> findByRecordKey(RecordKey recordKey);

    // 찾기
    @Transactional(readOnly= true)
    List<DownloadLog> findAllByOrderByRecordKeyDateTimeDesc();
    @Transactional(readOnly= true)
    List<DownloadLog> findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(User recordKey_userId);

    @Transactional(readOnly= true)
    List<DownloadLog> findAllByUserIpOrderByRecordKeyDateTimeDesc(String userIp);

    @Transactional(readOnly= true)
    List<DownloadLog> findAllByQuizIdOrderByRecordKeyDateTimeDesc(Quiz quiz);


    @Transactional
    void deleteByQuizId(Quiz quiz);
    @Transactional
    void deleteByRecordKeyUserId(User user);

    @Modifying
    @Query("DELETE FROM DownloadLog e WHERE e.recordKey.dateTime <= :dateTime")
    void deleteByRecordKeyDateTimeBefore(LocalDateTime dateTime);
}
