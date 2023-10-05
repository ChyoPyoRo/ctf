package kimdaehan.ctf.controller;


import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
        mv.addObject("challenge","PWN");


        return mv;
    }
}
