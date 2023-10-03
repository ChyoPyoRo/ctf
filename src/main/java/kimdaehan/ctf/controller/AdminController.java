package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController extends BaseController{

    public final UserService userService;
    public final QuizService quizService;
    public final ServerSettingService serverSettingService;

    @Autowired
    public AdminController(UserService userService, AuthenticationFacade authenticationFacade, UserService userService1, QuizService quizService, ServerSettingService serverSettingService) {
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.serverSettingService = serverSettingService;
    }


    @GetMapping({"/admin_main"})
    public ModelAndView adminMain(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        //서버 시간
        mv.addObject("startDate",serverSettingService.getServerStartDate());
        mv.addObject("startTime",serverSettingService.getStartTimeToString());
        mv.addObject("endDate",serverSettingService.getServerEndDate());
        mv.addObject("endTime",serverSettingService.getEndTimeToString());

        mv.setViewName("/admin/admin_main");
        return mv;
    }


    @GetMapping({"/admin_user"})
    public ModelAndView adminUser(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        List<User> users = userService.getAllUser();

        mv.addObject("users", users);
        mv.setViewName("/admin/admin_user");
        return mv;
    }

    // 어드민 문제 관리
    @GetMapping({"/admin_quiz"})
    public ModelAndView adminQuiz(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        List<Quiz> quizzes = quizService.getAllQuiz();

        mv.addObject("quizzes", quizzes);
        mv.setViewName("/admin/admin_quiz");
        return mv;
    }

    @PostMapping(value = {"/admin_quiz"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postAdminQuiz(HttpServletRequest request, @RequestBody Quiz quiz){
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        if(quizService.getQuiz(quiz.getQuizId()) != null){
            logger.error("already exist Quiz -> user : {}, quizName : {}", user.getUserId(), quiz.getQuizName());
            return ResponseEntity.badRequest().body("already exist data");
        }

        quizService.upsertQuiz(quiz);
        logger.error("Quiz Created -> user : {}, quizName : {}", user.getUserId(), quiz.getQuizName());
        return ResponseEntity.ok("success");
    }


}
