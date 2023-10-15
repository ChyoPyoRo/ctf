package kimdaehan.ctf.service;

import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.repository.AccessLogRepository;
import kimdaehan.ctf.repository.DownloadLogRepository;
import kimdaehan.ctf.repository.FlagLogRepository;
import kimdaehan.ctf.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    private final AccessLogRepository accessLogRepository;
    private final DownloadLogRepository downloadLogRepository;
    private final FlagLogRepository flagLogRepository;

    public Quiz getQuiz(UUID quizId){
        return quizRepository.findByQuizId(quizId).orElse(null);
    }
    //모든 Quiz 가져오기
    public List<Quiz> getAllQuiz(){
        return quizRepository.findAllByOrderByLevelAscRegistrationTimeAsc();
    }

    //카테고리별로 Quiz 가져오기
    public List<Quiz> getAllQuizByCategory(Quiz.CategoryType categoryType){
        return quizRepository.findAllByCategoryOrderByLevelAscRegistrationTimeAsc(categoryType);
    }
    //Quiz 저장
    public void upsertQuiz(Quiz quiz){
        quizRepository.save(quiz);
    }

    //Quiz 삭제
    @Transactional
    public void deleteQuiz(Quiz quiz){
        // 퀴즈와 관련된 로그 삭제
        accessLogRepository.deleteByQuizId(quiz);
        downloadLogRepository.deleteByQuizId(quiz);
        flagLogRepository.deleteByQuizId(quiz);

        // 퀴즈 삭제
        quizRepository.delete(quiz);
    }


    public void upsertQuizWithDto(Quiz quiz, QuizDto quizDto){
        quiz.setCategory(Quiz.CategoryType.valueOf(quizDto.getCategory()));
        quiz.setFlag(quizDto.getFlag());
        quiz.setDescription(quizDto.getDescription());
        quiz.setLevel(Integer.valueOf(quizDto.getLevel()));
        quiz.setStartTime(quizDto.getLocalDateTime());
        quiz.setQuizName(quizDto.getQuizName());
        quizRepository.save(quiz);
    }

    public void saveQuizScore(Quiz quiz, User user){

    }


}
