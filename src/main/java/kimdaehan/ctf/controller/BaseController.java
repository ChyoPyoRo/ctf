package kimdaehan.ctf.controller;

import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    public BaseController(UserService userService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    protected Authentication getAuthentication(){return authenticationFacade.getAuthentication();}

    protected String getUsername() {return getAuthentication().getName();}

    protected User getUser() {return userService.getUserId(getUsername());}

}
