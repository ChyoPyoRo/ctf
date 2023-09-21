package kimdaehan.ctf.controller;

import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CenterController extends BaseController{
    @Autowired
    public CenterController(UserService userService, AuthenticationFacade authenticationFacade) {
        super(userService, authenticationFacade);
    }




}
