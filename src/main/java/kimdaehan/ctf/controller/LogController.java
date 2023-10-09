package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.service.LogService;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class LogController extends BaseController{

    private final LogService logService;
    private final QuizService quizService;

    @Autowired
    public LogController(UserService userService, AuthenticationFacade authenticationFacade, LogService logService, QuizService quizService) {
        super(userService, authenticationFacade);
        this.logService = logService;
        this.quizService = quizService;
    }

    // 접속로그
    @GetMapping({"/log/access"})
    public ResponseEntity<?> userAccessLog(HttpServletRequest request, @Param("uuid") String uuid){
        User user = getUser();
        Quiz quiz = quizService.getQuiz(UUID.fromString(uuid));
        if(quiz == null){
            logger.error("User edit Challenge uuid -> user : {}",user.getUserId());
            return ResponseEntity.badRequest().body("Validation error");
        }
        logger.info("User access Challenge -> user : {}, quiz : {}",user.getUserId(),quiz.getQuizName());
        AccessLog accessLog = logService.buildAccessLogByQuizAndUserAndIP(quiz, user, request.getRemoteAddr());
        logService.upsertAccess(accessLog);
        return ResponseEntity.ok().body("success");
    }

}
