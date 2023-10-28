package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.Result;
import kimdaehan.ctf.dto.UserPageDTO;
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
    public ModelAndView getMain(HttpSession httpSession) {
        User user = getUser();
        ModelAndView mv = new ModelAndView("main");


        if(user == null){
            mv.addObject("user", null);
            mv.addObject("type", null);
        } else {
            if(user.getIsBan() == User.IsBan.ENABLE){
                httpSession.invalidate();
                mv.setViewName("error/481");
                return mv;
            }
            mv.addObject("user", user.getUserId());
            mv.addObject("type", user.getType());
        }
        return mv;
    }

    @GetMapping({"/myPage/profile"})
    public ModelAndView getMyPage() {
        User user = getUser();
        ModelAndView mv = new ModelAndView("/mypage/profile");
        UserPageDTO userByAffiliationAndUserId = userService.getUserByAffiliationAndUserId(user.getAffiliation().toString(), user.getUserId());
        mv.addObject("user", user.getUserId());
        mv.addObject("type", user.getType());
        mv.addObject("userInfo", user);
        mv.addObject("score",userByAffiliationAndUserId.getTotal_score());
        return mv;
    }
    @GetMapping({"/myPage/solved"})
    public ModelAndView getSolvedQuiz() {
        User user = getUser();
        ModelAndView mv = new ModelAndView("/mypage/solved");
        mv.addObject("user", user.getUserId());
        mv.addObject("type", user.getType());

        List<String> reversing = userService.getAllQuizNameByUserId(user.getUserId(),"REVERSING");
        List<String> pwn = userService.getAllQuizNameByUserId(user.getUserId(),"PWN");
        List<String> web = userService.getAllQuizNameByUserId(user.getUserId(),"WEB");
        List<String> forensics = userService.getAllQuizNameByUserId(user.getUserId(),"FORENSICS");
        List<String> misc = userService.getAllQuizNameByUserId(user.getUserId(),"MISC");

        mv.addObject("reversing", reversing);
        mv.addObject("pwn", pwn);
        mv.addObject("web", web);
        mv.addObject("forensics", forensics);
        mv.addObject("misc", misc);

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
        if(!(user.getNickName().equals(editUser.getNickName()))){
            User existNickName = userService.getUserByNickName(editUser.getNickName());
            if(existNickName != null){
                code = Result.Code.NICK_NAME_EXIST;
                return code;
            }
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
