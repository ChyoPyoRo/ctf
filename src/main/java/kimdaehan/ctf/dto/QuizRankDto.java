package kimdaehan.ctf.dto;

import kimdaehan.ctf.entity.User;

import java.time.LocalDateTime;

public interface QuizRankDto {

    String getNickName();
    LocalDateTime getSolvedTime();

    User.Affiliation getAffiliation();
}
