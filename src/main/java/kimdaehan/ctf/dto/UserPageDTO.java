package kimdaehan.ctf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public interface UserPageDTO {
    String getUser_id();
    String getName();
    String getAffiliation();
    String getNick_name();
    String getIs_ban();
    LocalDateTime getRegistration_date_time();
    Long getTotal_score();
}


