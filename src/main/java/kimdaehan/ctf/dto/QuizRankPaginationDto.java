package kimdaehan.ctf.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class QuizRankPaginationDto {
    private final List<QuizRankDto> quizResult;
    private final Integer page;

    public QuizRankPaginationDto(List<QuizRankDto> quizResult, Integer page){
        this.quizResult = quizResult;
        this.page = page;
    }
}
