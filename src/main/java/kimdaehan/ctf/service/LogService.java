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
import java.util.ArrayList;
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

    public DownloadLog buildDownloadLogByQuizAndUserIP(Quiz quiz, User user, String userIp){
        return DownloadLog.builder()
                .quizId(quiz)
                .userIp(userIp)
                .recordKey(new RecordKey(user, LocalDateTime.now()))
                .build();
    }

    public FlagLog buildFlagLogByQuizAndUserIPAndSuccessFail(Quiz quiz, User user, String userIp, String answer, FlagLog.SuccessOrNot successOrNot){
        return FlagLog.builder()
                .quizId(quiz)
                .userIp(userIp)
                .recordKey(new RecordKey(user, LocalDateTime.now()))
                .flag(answer)
                .successFail(successOrNot)
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

    public List<?> getLogByUserAndType(User user, String type){
        return switch (type) {
            case "ACCESS" -> accessLogRepository.findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(user);
            case "FLAG" -> flagLogRepository.findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(user);
            case "DOWNLOAD" -> downloadLogRepository.findAllByRecordKeyUserIdOrderByRecordKeyDateTimeDesc(user);
            default -> new ArrayList<>();
        };
    }

    public List<?> getLogByUserIpAndType(String userIp, String type){
        return switch (type) {
            case "ACCESS" -> accessLogRepository.findAllByUserIpOrderByRecordKeyDateTimeDesc(userIp);
            case "FLAG" -> flagLogRepository.findAllByUserIpOrderByRecordKeyDateTimeDesc(userIp);
            case "DOWNLOAD" -> downloadLogRepository.findAllByUserIpOrderByRecordKeyDateTimeDesc(userIp);
            default -> new ArrayList<>();
        };
    }

    public List<?> getLogByQuizAndType(Quiz quiz, String type){
        return switch (type) {
            case "ACCESS" -> accessLogRepository.findAllByQuizIdOrderByRecordKeyDateTimeDesc(quiz);
            case "FLAG" -> flagLogRepository.findAllByQuizIdOrderByRecordKeyDateTimeDesc(quiz);
            case "DOWNLOAD" -> downloadLogRepository.findAllByQuizIdOrderByRecordKeyDateTimeDesc(quiz);
            default -> new ArrayList<>();
        };
    }
    public List<FlagLog> getFlagLogBySuccessOrNot(FlagLog.SuccessOrNot successOrNot){
        return flagLogRepository.findAllBySuccessFailOrderByRecordKeyDateTimeDesc(successOrNot);
    }

    public List<FlagLog> getFlagLogByUserOneMinuteAgo(Quiz quizId, User user ){
        return flagLogRepository.findListBeforeOneMinute(user, quizId, LocalDateTime.now().minusMinutes(1));
    }
}
