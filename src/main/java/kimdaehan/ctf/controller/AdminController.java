package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.configuration.JsonIgnore;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.dto.ServerTime;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.service.LogService;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import kimdaehan.ctf.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final UserService userService;
    private final QuizService quizService;
    private final ServerSettingService serverSettingService;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminController(UserService userService, AuthenticationFacade authenticationFacade, UserService userService1, QuizService quizService, ServerSettingService serverSettingService, LogService logService, PasswordEncoder passwordEncoder) {
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.serverSettingService = serverSettingService;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
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

        //active
        mv.addObject("type","MAIN");

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
        //active
        mv.addObject("type","USER");
        mv.setViewName("/admin/admin_user");
        return mv;
    }
    @GetMapping({"/admin_user/detail/{userId}"})
    public ModelAndView adminUserDetail(HttpServletRequest request, @PathVariable String userId){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        // 검색 유저
        User member = userService.getUserId(userId);
        if(member == null){
            logger.error("No User Data -> user : {}", user.getUserId());
            mv.setViewName("/error/400");
            return mv;
        }
        mv.addObject("user", member);
        //active
        mv.addObject("type","USER");
        mv.setViewName("/admin/admin_user_detail");
        return mv;
    }
    @PostMapping({"/admin_user/detail/{userId}"})
    @ResponseBody
    public ResponseEntity<String> adminUserDetailPost(HttpServletRequest request, @PathVariable String userId, @RequestBody User member){
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        // 검색 유저
        User existMember = userService.getUserId(userId);
        if(existMember == null){
            logger.error("No User Data -> user : {}", user.getUserId());
            return ResponseEntity.badRequest().body("Validation error");
        }
        if(isMissingUserItem(member)) {
            logger.error("No User Data -> user : {}", user.getUserId());
            return ResponseEntity.badRequest().body("Validation error");
        }
        //유저 정보 변경
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        userService.changeUser(existMember, member);

        return ResponseEntity.ok("success");
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

        //active
        mv.addObject("type","QUIZ");

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
        //active
        mv.addObject("type","QUIZ");
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
        quiz.setQuizWriter(user);
        // 파일 저장 및 경로 저장
        if(quizDto.getFile() != null){
            String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
            String basePath = rootPath + "/../" + "ctf/data";
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
            String basePath = rootPath + "/../" + "ctf/data";
            String filePath = basePath + "/" + quizDto.getFile().getOriginalFilename();
            File dest = new File(filePath);
            quizDto.getFile().transferTo(dest);
            quiz.setAttachment(filePath);
        }
        // Quiz entity 변경점 적용
        quizService.upsertQuizWithDto(quiz, quizDto);
        return ResponseEntity.ok("success");
    }

    // 어드민 퀴즈 삭제
    @GetMapping(value = {"/admin_quiz/delete/{uuid}"})
    @ResponseBody
    public ResponseEntity<String> deleteAdminQuiz(HttpServletRequest request, @PathVariable String uuid ){
        User user = getUser();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            return ResponseEntity.badRequest().body("404 error");
        }
        Quiz quiz = quizService.getQuiz(UUID.fromString(uuid));
        if(quiz == null){
            return ResponseEntity.badRequest().body("Validation error");
        } else {
            logger.info("delete Quiz -> user : {}, quizName {}", user.getUserId(), quiz.getQuizName());
            quizService.deleteQuiz(quiz);
        }
        return ResponseEntity.ok("success");
    }
    //어드민 로그

    @GetMapping({"/admin_log/{logType}"})
    public ModelAndView adminLog(HttpServletRequest request, @PathVariable String logType){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(user.getType() != User.Type.ADMIN){
            logger.error("Not Admin access this page -> user : {}, IP : {}", user.getUserId(), request.getRemoteAddr());
            mv.setViewName("/error/404");
            return mv;
        }
        if(logType.equals("ACCESS")){
            mv.setViewName("/admin/log/admin_access_log");
        } else if(logType.equals("FLAG")){
            mv.setViewName("/admin/log/admin_flag_log");
        } else if(logType.equals("DOWNLOAD")){
            mv.setViewName("/admin/log/admin_download_log");
        } else {
            mv.setViewName("/error/404");
        }
        //active
        mv.addObject("type","LOG");
        return mv;
    }

    public boolean isMissingItem(QuizDto quizDto) {
        return Utility.nullOrEmptyOrSpace(quizDto.getQuizName()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getCategory()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getFlag()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getLevel().toString()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getStartDate().toString()) ||
                Utility.nullOrEmptyOrSpace(quizDto.getStartTime().toString());
    }
    public boolean isMissingUserItem(User user) {
        return Utility.nullOrEmptyOrSpace(user.getUserId()) ||
                Utility.nullOrEmptyOrSpace(user.getPassword()) ||
                Utility.nullOrEmptyOrSpace(user.getName()) ||
                Utility.nullOrEmptyOrSpace(String.valueOf(user.getAffiliation()))||
                Utility.nullOrEmptyOrSpace(String.valueOf(user.getType()));
    }

}
