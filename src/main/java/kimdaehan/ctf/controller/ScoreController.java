package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ScoreController extends  BaseController{

    public ScoreController(UserService userService, AuthenticationFacade authenticationFacade) {
        super(userService, authenticationFacade);
    }

    @GetMapping({"/score"})
    public ModelAndView scoreMain(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName(("/score/score"));
        mv.addObject("title", "ScoreBoard");
        return mv;
    }
}
