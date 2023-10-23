package kimdaehan.ctf.repository;


import kimdaehan.ctf.dto.DynamicScoreDTO;
import kimdaehan.ctf.dto.QuizListDTO;

import kimdaehan.ctf.dto.QuizOneDto;
import kimdaehan.ctf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import kimdaehan.ctf.entity.Quiz;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    @Transactional(readOnly= true)
    Optional<Quiz> findByQuizId(UUID quizId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT q.quizName, q.description, u.nickName AS author, q.score, q.startTime " +
            " FROM Quiz AS q " +
            " LEFT JOIN User u ON(q.quizWriter.userId = u.userId) " +
            " WHERE q.quizId = :quizId ")
    QuizOneDto findOneQuiz(@Param("quizId") UUID quizId);

    @Transactional(readOnly= true)
    List<Quiz> findAllByOrderByLevelAscRegistrationTimeAsc();

    @Transactional(readOnly= true)
    List<Quiz> findAllByCategoryOrderByLevelAscRegistrationTimeAsc(Quiz.CategoryType categoryType);

    @Transactional(readOnly = true)
    @Query(value = "SELECT HEX(q.quiz_id) AS quizId, " +
            "q.quiz_name as quizName, q.category AS category, q.score AS score, " +
            "CONVERT(u.nick_name USING utf8) AS author "
            + "FROM quiz q "
            +"LEFT JOIN user u ON (q.user_id = u.user_id)"
            +" WHERE category = :category AND start_time < :nowTime"
            , nativeQuery = true
    )
    List<QuizListDTO> findQuizListAfterStartTimeWithCategory(@Param("category") String category , @Param("nowTime") LocalDateTime nowTime);

    @Transactional
    void deleteByQuizWriter(User user);


    @Transactional(readOnly = true)
    @Query(value = "SELECT COALESCE(user_count, 0) AS user_count, " +
            "       COALESCE(solved_count, 0) AS solved_count, " +
            "       (quiz.max_score + (quiz.min_score - quiz.max_score) * ( (solved_count * solved_count) / (user_count * user_count))) AS calculated_score " +
            "FROM ( " +
            "    SELECT COUNT(*) AS user_count " +
            "    FROM user " +
            "    WHERE type = 'USER' AND affiliation IN ('YB', 'NB') AND is_ban = 'DISABLE' " +
            ") AS u " +
            "CROSS JOIN ( " +
            "    SELECT COUNT(*) AS solved_count,solved_quiz_id " +
            "    FROM solved " +
            "    WHERE solved_quiz_id = :quizId " +
            "    GROUP BY solved_quiz_id " +
            ") AS s " +
            "JOIN quiz ON quiz.quiz_id = s.solved_quiz_id;", nativeQuery = true)
    Optional<DynamicScoreDTO> findDynamicScore(@Param("quizId") UUID quizId);

    @Transactional
    @Modifying
    @Query("UPDATE Quiz q " +
            "SET q.score =  :score "+
            " WHERE q.quizId = :quizId")
    void updateQuizScore(@Param("quizId") UUID quizId, @Param("score") Integer score);
}
