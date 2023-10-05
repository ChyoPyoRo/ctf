package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController extends BaseController{

    public final UserService userService;
    public final QuizService quizService;
    public final ServerSettingService serverSettingService;

    @Autowired
    public AdminController(UserService userService, AuthenticationFacade authenticationFacade, UserService userService1, QuizService quizService, ServerSettingService serverSettingService) {
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.serverSettingService = serverSettingService;
    }


    @GetMapping({"/admin_main"})
    public ModelAndView adminMain(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        //서버 시간
        mv.addObject("startDate",serverSettingService.getServerStartDate());
        mv.addObject("startTime",serverSettingService.getStartTimeToString());
        mv.addObject("endDate",serverSettingService.getServerEndDate());
        mv.addObject("endTime",serverSettingService.getEndTimeToString());

        mv.setViewName("/admin/admin_main");
        return mv;
    }


    @GetMapping({"/admin_user"})
    public ModelAndView adminUser(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        List<User> users = userService.getAllUser();

        mv.addObject("users", users);
        mv.setViewName("/admin/admin_user");
        return mv;
    }

    // 어드민 문제 관리
    @GetMapping({"/admin_quiz"})
    public ModelAndView adminQuiz(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        List<Quiz> quizzes = quizService.getAllQuiz();

        mv.addObject("quizzes", quizzes);
        mv.setViewName("/admin/admin_quiz");
        return mv;
    }

    @GetMapping({"/admin_quiz/create"})
    public ModelAndView adminQuizCreate(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }

        mv.setViewName("/admin/admin_quiz_create");
        return mv;
    }
    // 어드민 퀴즈 생성
    @PostMapping(value = {"/admin_quiz/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> postAdminQuiz(HttpServletRequest request, @ModelAttribute QuizDto quizDto) throws IOException {
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        Quiz quiz = quizDto.dtoToQuiz();
        quiz.setQuizWriter(user.getUserId());
        System.out.println("quiz.getRegistrationTime() = " + quiz.getRegistrationTime());
        System.out.println("quiz.getQuizId() = " + quiz.getQuizId());
        // 파일 저장 및 경로 저장
        if(quizDto.getFile() != null){
            String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
            String basePath = rootPath + "/" + "data";
            String filePath = basePath + "/" + quizDto.getFile().getOriginalFilename();
            File dest = new File(filePath);
            quizDto.getFile().transferTo(dest);
            quiz.setAttachment(filePath);
        }
        
        return ResponseEntity.ok("success");
    }




}
