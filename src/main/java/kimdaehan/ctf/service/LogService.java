package kimdaehan.ctf.service;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
import kimdaehan.ctf.repository.AccessLogRepository;
import kimdaehan.ctf.repository.DownloadLogRepository;
import kimdaehan.ctf.repository.FlagLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService {
    private final FlagLogRepository flagLogRepository;
    private final AccessLogRepository accessLogRepository;
    private final DownloadLogRepository downloadLogRepository;


    // 로그 저장
    public void upsertFlag(FlagLog flagLog){
        flagLogRepository.save(flagLog);
    }

    public void upsertAccess(AccessLog accessLog){
        accessLogRepository.save(accessLog);
    }
    public void upsertDownload(DownloadLog downloadLog){
        downloadLogRepository.save(downloadLog);
    }

    // 로그 빌더
    public AccessLog buildAccessLogByQuizAndUserAndIP(Quiz quiz, User user, String userIp){
        return AccessLog.builder()
                .quizId(quiz)
                .userIp(userIp)
                .recordKey(new RecordKey(user, LocalDateTime.now()))
                .build();
    }



    //로그 검색
    public List<FlagLog> getAllFlagLog(){
        return flagLogRepository.findAllByOrderByRecordKeyDateTimeDesc();
    }

    public List<AccessLog> getAllAccessLog(){
        return accessLogRepository.findAllByOrderByRecordKeyDateTimeDesc();
    }
    public List<DownloadLog> getAllDownloadLog(){
        return downloadLogRepository.findAllByOrderByRecordKeyDateTimeDesc();
    }
}
