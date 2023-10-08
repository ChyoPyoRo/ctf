package kimdaehan.ctf.service;

import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
import kimdaehan.ctf.repository.AccessLogRepository;
import kimdaehan.ctf.repository.DownloadLogRepository;
import kimdaehan.ctf.repository.FlagLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    //로그 검색
    public List<FlagLog> getAllFlagLog(){
        return flagLogRepository.findAll();
    }

    public List<AccessLog> getAllAccessLog(){
        return accessLogRepository.findAll();
    }
    public List<DownloadLog> getAllDownloadLog(){
        return downloadLogRepository.findAll();
    }
}
