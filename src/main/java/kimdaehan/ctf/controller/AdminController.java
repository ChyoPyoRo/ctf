package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController extends BaseController{

    @Autowired
    public AdminController(UserService userService, AuthenticationFacade authenticationFacade) {
        super(userService, authenticationFacade);
    }

    @GetMapping({"/admin_main"})
    public ModelAndView adminMain(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/404");
            return mv;
        }

        mv.setViewName("/admin/admin_main");
        return mv;
    }
}
