package kimdaehan.ctf.controller;


import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Controller
public class QuizController extends BaseController{

    public final UserService userService;

    public final QuizService quizService;

    public final ServerSettingService serverSettingService;
    @Autowired
    public QuizController(UserService userService, AuthenticationFacade authenticationFacade, QuizService quizService , UserService userService1, ServerSettingService serverSettingService){
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.serverSettingService = serverSettingService;
    };

    @GetMapping({"/challenge"})
    public ModelAndView challengeMain(HttpServletRequest request){
        //메인 페이지
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/quiz/quiz_main");
        ArrayList <String> categoryList = new ArrayList<>(Arrays.asList("REVERSING", "PWN", "WEB", "FORENSICS", "MISC", "CRYPTO"));
        for(String item : categoryList){
            Quiz.CategoryType categoryName = Quiz.CategoryType.valueOf(item);
            List<Quiz> quizList = quizService.getAllQuizByCategory(categoryName);
            mv.addObject(item, quizList);
        }

        return mv;
    }

    @GetMapping({"/quiz/{quizId}"})
    public ResponseEntity<Quiz> quizListByCategory(@PathVariable String quizId){
        UUID uuid = UUID.fromString(quizId);
        Quiz quizDetail = quizService.getQuiz(uuid);
        quizDetail.setFlag("나쁜짓 하지 마세요");
        return ResponseEntity.ok(quizDetail);
    }


    @GetMapping({"/test/{quizId}"})
    public ResponseEntity<Quiz> test(@PathVariable String quizId){
        UUID uuid = UUID.fromString(quizId);
        Quiz quizDetail = quizService.getQuiz(uuid);
        quizDetail.setFlag("나쁜짓 하지 마세요");
        return ResponseEntity.ok(quizDetail);
    }
}
