package kimdaehan.ctf.service;

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
}
