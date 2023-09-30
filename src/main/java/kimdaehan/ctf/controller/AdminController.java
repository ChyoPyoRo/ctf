package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController extends BaseController{

    public final UserService userService;

    @Autowired
    public AdminController(UserService userService, AuthenticationFacade authenticationFacade, UserService userService1) {
        super(userService, authenticationFacade);
        this.userService = userService1;
    }

    @GetMapping({"/admin_main"})
    public ModelAndView adminMain(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("error");
            return mv;
        }

        mv.setViewName("/admin/admin_main");
        return mv;
    }


    @GetMapping({"/admin_user"})
    public ModelAndView adminUser(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("error");
            return mv;
        }
        List<User> users = userService.getAllUser();

        mv.addObject("users", users);
        mv.setViewName("/admin/admin_user");
        return mv;
    }
}
