package kimdaehan.ctf.dto;

import java.time.LocalDateTime;

public interface RankGraphDTO {
    String getUserId();
    Integer getUserRank();
    Integer getScore();
    LocalDateTime getDateTime();
}