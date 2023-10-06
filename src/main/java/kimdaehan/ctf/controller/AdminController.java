package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import kimdaehan.ctf.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @GetMapping({"/admin_quiz/{crud}"})
    public ModelAndView adminQuizCreate(HttpServletRequest request, @PathVariable String crud, @RequestParam(value = "uuid", required = false) String uuid){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        if (crud.equals("create")){
            logger.info("User Access /admin/quiz/create -> user : {}", user.getUserId());
            mv.setViewName("/admin/admin_quiz_create");
            return mv;
        } else if(crud.equals("edit")){
            logger.info("User Access /admin/quiz/edit -> user : {}", user.getUserId());
            if(uuid != null) {
                Quiz quiz = quizService.getQuiz(UUID.fromString(uuid));
                if(quiz != null){
                    if(quiz.getAttachment() != null){
                        File file = new File(quiz.getAttachment());
                        mv.addObject("file", file);
                    } else {
                        mv.addObject("file", null);
                    }
                    mv.addObject("date", quiz.getStartTime().toLocalDate());
                    mv.addObject("time", quiz.getStartTime().toLocalTime());
                    mv.addObject("quiz", quiz);
                    mv.setViewName("/admin/admin_quiz_edit");
                } else {
                    mv.setViewName("/error/400");
                }
            } else {
                mv.setViewName("/error/400");
            }
        } else {
            logger.error("User Access /admin/quiz/??? -> user : {}", user.getUserId());
            mv.setViewName("/error/404");
        }
        return mv;
    }
    // 어드민 퀴즈 생성
    @PostMapping(value = {"/admin_quiz/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> postAdminQuiz(HttpServletRequest request, @ModelAttribute QuizDto quizDto) throws IOException {
        User user = getUser();
        logger.info("User access");
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        if(quizDto != null && quizDto.getQuizId() != null){
            logger.error("quizId NonNull error -> user : {}", user.getUserId());
            return ResponseEntity.badRequest().body("Validation failed");
        } else if(quizDto != null) {
            //데이터 확인
            if(isMissingItem(quizDto)){
                logger.error("quizDto Validation error -> user : {}", user.getUserId());
                return ResponseEntity.badRequest().body("check data");
            }
        } else {
            logger.error("quizDto null error -> user : {}", user.getUserId());
            return ResponseEntity.badRequest().body("Validation failed");
        }
        Quiz quiz = quizDto.dtoToQuiz();
        quiz.setQuizWriter(user.getUserId());
        // 파일 저장 및 경로 저장
        if(quizDto.getFile() != null){
            String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
            String basePath = rootPath + "/" + "data";
            String filePath = basePath + "/" + quizDto.getFile().getOriginalFilename();
            File dest = new File(filePath);
            quizDto.getFile().transferTo(dest);
            quiz.setAttachment(filePath);
        }
        quizService.upsertQuiz(quiz);
        return ResponseEntity.ok("success");
    }

    // 어드민 퀴즈 수정
    @PostMapping(value = {"/admin_quiz/edit"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> postAdminQuizEdit(HttpServletRequest request, @ModelAttribute QuizDto quizDto) throws IOException {
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        if(quizDto.getQuizId() == null){
            return ResponseEntity.badRequest().body("Validation failed");
        } else {
            if(isMissingItem(quizDto)){
                return ResponseEntity.badRequest().body("check data");
            }
            if(quizService.getQuiz(UUID.fromString(quizDto.getQuizId())) == null){
                return ResponseEntity.badRequest().body("Validation failed");
            }
        }

        Quiz quiz = quizService.getQuiz(UUID.fromString(quizDto.getQuizId()));
        // 파일 저장 및 경로 저장
        if(quiz.getAttachment() != null){
            File existFile = new File(quiz.getAttachment());
            boolean result = existFile.delete();
            if(result){
                quiz.setAttachment(null);
            }
        }
        if(quizDto.getFile() != null){
            String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
            String basePath = rootPath + "/" + "data";
            String filePath = basePath + "/" + quizDto.getFile().getOriginalFilename();
            File dest = new File(filePath);
            quizDto.getFile().transferTo(dest);
            quiz.setAttachment(filePath);
        }
        // Quiz entity 변경점 적용
        quizService.upsertQuizWithDto(quiz, quizDto);
        return ResponseEntity.ok("success");
    }

    public boolean isMissingItem(QuizDto quizDto) {
        return Utility.nullOrEmptyOrSpace(quizDto.getQuizName()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getCategory()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getFlag()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getLevel().toString()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getStartDate().toString()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getStartTime().toString());
    }

}
