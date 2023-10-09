package kimdaehan.ctf.repository;


import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import kimdaehan.ctf.entity.Quiz;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    @Transactional(readOnly= true)
    Optional<Quiz> findByQuizId(UUID quizId);

    @Transactional(readOnly= true)
    List<Quiz> findAllByOrderByLevelAscRegistrationTimeAsc();

    @Transactional(readOnly= true)
    List<Quiz> findAllByCategoryOrderByLevelAscRegistrationTimeAsc(Quiz.CategoryType categoryType);

    @Transactional
    void deleteByQuizWriter(User user);

}
