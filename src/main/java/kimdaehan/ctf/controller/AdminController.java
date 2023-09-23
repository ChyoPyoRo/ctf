package kimdaehan.ctf.controller;

import kimdaehan.ctf.auth.AuthenticationFacade;
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
    public ModelAndView adminMain(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/admin_main");
        return mv;
    }
}
