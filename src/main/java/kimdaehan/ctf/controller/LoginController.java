package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.Result;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.UserService;
import kimdaehan.ctf.util.Utility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
@Controller
@RequiredArgsConstructor
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping({ "/login"})
    public ModelAndView getLogin(HttpServletRequest request){

        ModelAndView mv = new ModelAndView("login/login");
        mv.addObject("type",null);
        return mv;
    }

    @GetMapping({"/register"})
    public ModelAndView getRegister(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("login/register");
        mv.addObject("type",null);
        return mv;
    }



    //아직 안만듬
    @PostMapping(value = "/saveUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result.Code putUser(@RequestBody User user) {
        logger.info("user : {}",user.getUserId());
        Result.Code code = Result.Code.ERROR;

        if(!isMissingMandatories(user)) {
            try{
                logger.info("try saveUser id : {}",user.getUserId());

                User existId = userService.getUserId(user.getUserId()); // 아이디 있는지 확인
                logger.info("existId : {}", existId);

                if(existId == null) {
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPassword);
                    user.setType(User.Type.USER);

                    userService.upsertUser(user); //회원가입
                    code = Result.Code.OK;
                    logger.info("Created User : {}", user.getUserId());
                } else {
                    code = Result.Code.ID_EXIST;
                }

            } catch (Exception exception){
                logger.error(exception.getLocalizedMessage(), exception);
                code = Result.Code.ERROR;
                logger.error("code : {}",code);
            }
        }
        return code;
    }

    public boolean isMissingMandatories(User user) {
        return Utility.nullOrEmptyOrSpace(user.getUserId()) ||
                Utility.nullOrEmptyOrSpace(user.getPassword()) ||
                Utility.nullOrEmptyOrSpace(user.getName()) ||
                Utility.nullOrEmptyOrSpace(String.valueOf(user.getAffiliation()));
    }

}