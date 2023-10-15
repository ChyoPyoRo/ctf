package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.Result;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.UserService;
import kimdaehan.ctf.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class CenterController extends BaseController{
    private final PasswordEncoder passwordEncoder;
    private final QuizService quizService;

    @Autowired
    public CenterController(UserService userService, AuthenticationFacade authenticationFacade, PasswordEncoder passwordEncoder, QuizService quizService) {
        super(userService, authenticationFacade);
        this.passwordEncoder = passwordEncoder;
        this.quizService = quizService;
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

    @GetMapping({"/myPage"})
    public ModelAndView getMyPage() {
        User user = getUser();
        ModelAndView mv = new ModelAndView("/info");
        List<Solved> solvedList = quizService.getSolvedListByUserId(user.getUserId());
        mv.addObject("user", user.getUserId());
        mv.addObject("type", user.getType());
        mv.addObject("userInfo", user);
        mv.addObject("solvedList", solvedList);
        return mv;
    }


    @PostMapping(value = "/editUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result.Code editUser(@RequestBody User editUser, HttpServletRequest request) {
        User user = getUser();
        logger.info("try User edit -> user : {}, IP : {}",user.getUserId(),request.getRemoteAddr());
        Result.Code code = Result.Code.ERROR;
        if(!editUser.getUserId().equals(user.getUserId())){
            return code;
        }
        if(!isMissingItem(user)){
            try{
                userSet(editUser, user);
                userService.upsertUser(user); //유저 정보 변경
                code = Result.Code.OK;
                logger.info("Created User : {}", user.getUserId());
            } catch (Exception exception){
                logger.error(exception.getLocalizedMessage(), exception);
                code = Result.Code.ERROR;
                logger.error("code : {}",code);
            }
        }
        return code;
    }

    private void userSet(User editUser, User user) {
        String encodedPassword = passwordEncoder.encode(editUser.getPassword());
        user.setPassword(encodedPassword);
        user.setAffiliation(editUser.getAffiliation());
        user.setName(editUser.getName());
        user.setNickName(editUser.getNickName());
    }

    public boolean isMissingItem(User user) {
        return Utility.nullOrEmptyOrSpace(user.getUserId()) ||
                Utility.nullOrEmptyOrSpace(user.getPassword()) ||
                Utility.nullOrEmptyOrSpace(user.getName()) ||
                Utility.nullOrEmptyOrSpace(String.valueOf(user.getAffiliation()));
    }
}
