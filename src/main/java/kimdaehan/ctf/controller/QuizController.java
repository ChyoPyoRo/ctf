package kimdaehan.ctf.controller;


import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.DynamicScoreDTO;
import kimdaehan.ctf.dto.QuizAnswerDto;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.SolvedId;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.QuizService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Controller
public class QuizController extends BaseController{

    public final UserService userService;

    public final QuizService quizService;

    public final ServerSettingService serverSettingService;
    @Autowired
    public QuizController(UserService userService, AuthenticationFacade authenticationFacade, QuizService quizService , UserService userService1, ServerSettingService serverSettingService){
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.serverSettingService = serverSettingService;
    };

    @GetMapping({"/challenge"})
    public ModelAndView challengeMain(HttpServletRequest request){
        //메인 페이지
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/quiz/quiz_main");
        ArrayList <String> categoryList = new ArrayList<>(Arrays.asList("REVERSING", "PWN", "WEB", "FORENSICS", "MISC"));
        for(String item : categoryList){
            Quiz.CategoryType categoryName = Quiz.CategoryType.valueOf(item);
            List<Quiz> quizList = quizService.getAllQuizByCategory(categoryName);
            mv.addObject(item, quizList);
        }
        mv.addObject("title", "Challenge");

        return mv;
    }

    @GetMapping({"/quiz/{quizId}"})
    public ResponseEntity<Quiz> quizListByCategory(@PathVariable String quizId){
        UUID uuid = UUID.fromString(quizId);
        Quiz quizDetail = quizService.getQuiz(uuid);
        quizDetail.setFlag("나쁜짓 하지 마세요");
        return ResponseEntity.ok(quizDetail);
    }

    @PostMapping({"/challenge/{challengeId}"})
    @ResponseBody
    public ResponseEntity<String> solveQuiz(@PathVariable String challengeId, QuizAnswerDto answer) {
        //user정보, quiz 정보 가져오기
        User user = getUser();
        UUID quizId;
        try {
            quizId = UUID.fromString(challengeId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("WrongID");
        }
        Quiz quiz = quizService.getQuiz(quizId);
        //quiz값이 없을 경우 (존재하지 않는 ID)
        if(quiz == null){
            return ResponseEntity.badRequest().body("NotExistID");
        }
        if(serverSettingService.getServerEnd().isBefore( LocalDate.now().atTime(LocalTime.now().plusHours(2)))  ){
            //에러 문구
            return ResponseEntity.badRequest().body("ctfFinish");
        }
        //2.이 문제의 start time이 현재시간 이후이면 안됨
        else if(quiz.getStartTime().isAfter(LocalDate.now().atTime(LocalTime.now().plusHours(2)))){
            System.out.println(serverSettingService.getServerEnd());
            System.out.println(serverSettingService.getServerStart());
            System.out.println(LocalDate.now().atTime(LocalTime.now().plusHours(2)));
            return ResponseEntity.badRequest().body("notOpen");
        }
        //flag값 비교
        if (quiz.getFlag().equals(answer.getFlag())) {
            //flag 값이 일치하기 전에 시간이 먼저 테스트
            Solved solved = Solved.builder()
                    .solvedId(new SolvedId(quiz.getQuizId(), user.getUserId()))
                    .build();
            solved.setSolved(quiz);
            quizService.upsertSolvedQuiz(solved);
            //OB, ADMIN, isBan 3개는 점수 추가 안하고 로그만 남기기
            return ResponseEntity.ok("Correct");
        } else {
            //일치하지 않으면
            return ResponseEntity.ok("Wrong");
        }
    }

    @GetMapping({"/test/{quizId}"})
    public ResponseEntity<?> test(@PathVariable String quizId){
        User user = getUser();
        Quiz quiz = quizService.getQuiz(UUID.fromString(quizId));
        Solved solved = Solved.builder()
                .solvedId(new SolvedId(quiz.getQuizId(), user.getUserId()))
                .build();
        solved.setSolved(quiz);
        quizService.upsertSolvedQuiz(solved);
        return ResponseEntity.ok("good");
    }


    @GetMapping({"/test2/{quizId}"})
    public ResponseEntity<?> test2(@PathVariable String quizId){
        User user = getUser();
        DynamicScoreDTO dynamicScoreDTO = quizService.getDynamicScoreByQuizId(UUID.fromString(quizId));
        return ResponseEntity.ok(dynamicScoreDTO);
    }
}
