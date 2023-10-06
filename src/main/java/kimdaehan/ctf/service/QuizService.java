package kimdaehan.ctf.service;

import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;


    public Quiz getQuiz(UUID quizId){
        return quizRepository.findByQuizId(quizId).orElse(null);
    }
    public List<Quiz> getAllQuiz(){
        return quizRepository.findAll();
    }

    public void upsertQuiz(Quiz quiz){
        quizRepository.save(quiz);
    }


    public void upsertQuizWithDto(Quiz quiz, QuizDto quizDto){
        quiz.setCategory(Quiz.CategoryType.valueOf(quizDto.getCategory()));
        quiz.setFlag(quizDto.getFlag());
        quiz.setDescription(quizDto.getDescription());
        quiz.setLevel(Quiz.levelType.valueOf(quizDto.getLevel()));
        quiz.setStartTime(quizDto.getLocalDateTime());
        quiz.setQuizName(quizDto.getQuizName());
        quizRepository.save(quiz);
    }
}
