package kimdaehan.ctf.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Getter
@Setter
public class ServerSettingService {
    LocalDate serverStartDate = LocalDate.now();
    LocalTime serverStartTime = LocalTime.now();

    LocalDate serverEndDate = LocalDate.now().plusDays(1);
    LocalTime serverEndTime = LocalTime.now();

    public LocalDateTime getServerStart(){
        return this.serverStartDate.atTime(this.serverStartTime);
    }

    public LocalDateTime getServerEnd(){
        return this.serverEndDate.atTime(this.serverEndTime);
    }


    public String getStartTimeToString(){
        // 소수점 이하 자리를 버린 문자열로 변환
        return this.serverStartTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getEndTimeToString(){
        // 소수점 이하 자리를 버린 문자열로 변환
        return this.serverEndTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
