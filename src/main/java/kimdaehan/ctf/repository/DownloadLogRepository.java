package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog, RecordKey> {
    @Transactional(readOnly= true)
    Optional<DownloadLog> findByRecordKey(RecordKey recordKey);

    @Transactional(readOnly= true)
    List<DownloadLog> findAllByOrderByRecordKeyDateTimeDesc();
    @Transactional
    void deleteByQuizId(Quiz quiz);
    @Transactional
    void deleteByRecordKeyUserId(User user);
}
