package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.ServerTime;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
import kimdaehan.ctf.service.LogService;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class AdminAPIController extends BaseController{

    public final ServerSettingService serverSettingService;
    public final QuizService quizService;
    public final LogService logService;
    @Autowired
    public AdminAPIController(UserService userService, AuthenticationFacade authenticationFacade, ServerSettingService serverSettingService, QuizService quizService, LogService logService) {
        super(userService, authenticationFacade);
        this.serverSettingService = serverSettingService;
        this.quizService = quizService;
        this.logService = logService;
    }







    @PostMapping({"/server_setting"})
    public ResponseEntity<String> adminMain(HttpServletRequest request, @RequestBody ServerTime time){
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        if(time != null){
            if (time.getType().equals("START")){
                if(time.getServerTime() != null && time.getServerDate() != null){
                    serverSettingService.setServerStartDate(time.getServerDate());
                    serverSettingService.setServerStartTime(time.getServerTime());
                }
            } else if (time.getType().equals("END")) {
                if(time.getServerTime() != null && time.getServerDate() != null){
                    serverSettingService.setServerEndDate(time.getServerDate());
                    serverSettingService.setServerEndTime(time.getServerTime());
                }
            } else {
                return ResponseEntity.badRequest().body("Validation failed");
            }
            logger.info("Server Start time set -> user : {}, start_time : {}, end_time : {}", user.getUserId(),serverSettingService.getServerStart(), serverSettingService.getServerEnd());
        } else {
            return ResponseEntity.badRequest().body("time is Null Data");
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping({"/admin_quiz_list/{category}"})
    public ResponseEntity<?> getAdminQuiz(HttpServletRequest request, @PathVariable String category) {
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        List<Quiz> quizzes;

        switch (category) {
            case "ALL" -> quizzes = quizService.getAllQuiz();
            case "REVERSING" -> quizzes = quizService.getAllQuizByCategory(Quiz.CategoryType.REVERSING);
            case "WEB" -> quizzes = quizService.getAllQuizByCategory(Quiz.CategoryType.WEB);
            case "PWN" -> quizzes = quizService.getAllQuizByCategory(Quiz.CategoryType.PWN);
            case "FORENSICS" -> quizzes = quizService.getAllQuizByCategory(Quiz.CategoryType.FORENSICS);
            case "MISC" -> quizzes = quizService.getAllQuizByCategory(Quiz.CategoryType.MISC);
            default -> {
                return ResponseEntity.badRequest().body("Validation error");
            }
        }
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping({"/admin_log_list/{logType}"})
    public ResponseEntity<?> adminLog(HttpServletRequest request, @PathVariable String logType){
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        List<?> logData;
        switch (logType) {
            case "ACCESS" -> {
                logData = logService.getAllAccessLog();
            }
            case "FLAG" -> {
                logData = logService.getAllFlagLog();
            }
            case "DOWNLOAD" -> {
                logData = logService.getAllDownloadLog();
            }
            default -> {
                return ResponseEntity.badRequest().body("Validation error");
            }
        }
        return ResponseEntity.ok(logData);
    }

}
