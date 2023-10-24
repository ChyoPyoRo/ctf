package kimdaehan.ctf.dto;

import java.time.LocalDateTime;

public interface RankAllDTO {
    String getUserId();
    String getAffiliation();
    String getNickName();
    Long getScore();
    LocalDateTime getSolvedTime();
    Integer getSolvedCount();
}
