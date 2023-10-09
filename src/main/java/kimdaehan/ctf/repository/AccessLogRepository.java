package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, RecordKey> {

    @Transactional(readOnly= true)
    Optional<AccessLog> findByRecordKey(RecordKey recordKey);

    @Transactional(readOnly= true)
    List<AccessLog> findAllByOrderByRecordKeyDateTimeDesc();

    @Transactional
    void deleteByQuizId(Quiz quiz);
    @Transactional
    void deleteByRecordKeyUserId(User user);
}
