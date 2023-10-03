package kimdaehan.ctf.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import kimdaehan.ctf.entity.Quiz;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    @Transactional(readOnly= true)
    Optional<Quiz> findByQuizId(UUID quizId);
}