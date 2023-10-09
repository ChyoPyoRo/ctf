package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlagLogRepository extends JpaRepository<FlagLog, RecordKey> {
    @Transactional(readOnly= true)
    Optional<FlagLog> findByRecordKey(RecordKey recordKey);

    @Transactional(readOnly= true)
    List<FlagLog> findAllByOrderByRecordKeyDateTimeDesc();
}
