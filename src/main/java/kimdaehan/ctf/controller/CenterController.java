package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CenterController extends BaseController{
    @Autowired
    public CenterController(UserService userService, AuthenticationFacade authenticationFacade) {
        super(userService, authenticationFacade);
    }

    @GetMapping({"/"})
    public ModelAndView getMain() {
        User user = getUser();
        ModelAndView mv = new ModelAndView("main");


        if(user == null){
            mv.addObject("user", null);
            mv.addObject("type", null);
        } else {
            mv.addObject("user", user.getUserId());
            mv.addObject("type", user.getType());
        }
        return mv;
    }

}
