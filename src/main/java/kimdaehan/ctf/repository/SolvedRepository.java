package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.SolvedId;
import kimdaehan.ctf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Native;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolvedRepository extends JpaRepository<Solved, SolvedId> {

    @Transactional(readOnly= true)
    List<Solved> findAllBySolvedIdSolvedUserId(String userId);

    @Transactional(readOnly= true)
    List<Solved> findAllBySolved(Quiz quiz);


    /*@Transactional(readOnly = true)
    @Query(value = "SELECT solved_user_id, SUM(q.score) as total_score " +
            "FROM solved s " +
            "JOIN quiz q ON s.solved_quiz_id = q.quiz_id " +
            "WHERE s.solved_user_id = :userId;", nativeQuery = true)
    */
}
