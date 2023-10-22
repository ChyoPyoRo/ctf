package kimdaehan.ctf.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.DynamicScoreDTO;
import kimdaehan.ctf.dto.QuizAnswerDto;
import kimdaehan.ctf.dto.QuizDto;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.SolvedId;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.log.AccessLog;
import kimdaehan.ctf.entity.log.DownloadLog;
import kimdaehan.ctf.entity.log.FlagLog;
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

import kimdaehan.ctf.service.LogService;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
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
    public QuizController(UserService userService, AuthenticationFacade authenticationFacade, QuizService quizService , LogService logService, UserService userService1, ServerSettingService serverSettingService){
        super(userService, authenticationFacade);
        this.userService = userService1;
        this.quizService = quizService;
        this.logService = logService;
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
    public ResponseEntity<?> quizListByCategory(@PathVariable String quizId,  HttpServletRequest request){
        User user = getUser();
        UUID uuid = UUID.fromString(quizId);
        Quiz quizDetail = quizService.getQuiz(uuid);
        //start시간 제출이 안되면
        if(quizDetail.getStartTime().isAfter(LocalDate.now().atTime(LocalTime.now().plusHours(2)))){
            //logger 생성
            logger.info("Access try before start time -> user : {}", user.getUserId());
            return ResponseEntity.badRequest().body("notOpen");
        }
        //로그 기록
        AccessLog accessLog = logService.buildAccessLogByQuizAndUserAndIP(quizDetail, user, request.getRemoteAddr());
        logService.upsertAccess(accessLog);
        quizDetail.setFlag("나쁜짓 하지 마세요");
        return ResponseEntity.ok(quizDetail);
    }

    @PostMapping({"/challenge/{challengeId}"})
    @ResponseBody
    public ResponseEntity<String> solveQuiz(@PathVariable String challengeId, QuizAnswerDto answer, HttpServletRequest request) {
        //user정보, quiz 정보 가져오기
        User user = getUser();
        UUID quizId;
        //id의 형식이 uuid가 맞는지
        try {
            quizId = UUID.fromString(challengeId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("WrongID");
        }
        //로그 조회 -> uuid의 형식이 아니면 로그 조회가 불가능
//        logService.getLogByUserAndType(user, quizId);

        //flag값이 비어있음
        if(answer.getFlag() == null){
            //logger 남겨야 하나 + isBan어떻게 하지
            return ResponseEntity.badRequest().body("emptyFlag");
        }
        Quiz quiz = quizService.getQuiz(quizId);
        //quiz값이 없을 경우 (존재하지 않는 ID)
        if(quiz == null){
            return ResponseEntity.badRequest().body("NotExistID");
        }
        if(serverSettingService.getServerEnd().isBefore( LocalDate.now().atTime(LocalTime.now().plusHours(2)))  ){
            //시간이 지났는데 제출 시도, logger 남겨야 되나

            return ResponseEntity.badRequest().body("ctfFinish");
        }
        //2.이 문제의 start time이 현재시간 이후이면 안됨 >> get으로 옮김

        //flag값 비교
        if (quiz.getFlag().equals(answer.getFlag())) {
            //flag 값이 일치하기 전에 시간이 먼저 테스트
            Solved solved = Solved.builder()
                    .solvedId(new SolvedId(quiz.getQuizId(), user.getUserId()))
                    .build();
            solved.setSolved(quiz);
            quizService.upsertSolvedQuiz(solved);
            //OB, ADMIN, isBan 3개는 점수 추가 안하고 로그만 남기기
            //점수 추가
            //로그 남기기
            FlagLog.SuccessOrNot successOrNot = FlagLog.SuccessOrNot.SUCCESS;
            FlagLog flagLog = logService.buildFlagLogByQuizAndUserIPAndSuccessFail(quiz, user, request.getRemoteAddr(),successOrNot);
            logService.upsertFlag(flagLog);
            //user의 currentSolvedDateTime수정
            userService.changeUserCurrentSolvedDateTime(user);
            return ResponseEntity.ok("Correct");
        } else {
            //일치하지 않으면
            //로그만
            FlagLog.SuccessOrNot successOrNot = FlagLog.SuccessOrNot.FAIL;
            FlagLog flagLog = logService.buildFlagLogByQuizAndUserIPAndSuccessFail(quiz, user, request.getRemoteAddr(),successOrNot);
            logService.upsertFlag(flagLog);
            return ResponseEntity.ok("Wrong");
        }
    }
    @GetMapping({"/quiz/download/{quizId}"})
    public void download(HttpServletResponse response, @PathVariable String quizId, HttpServletRequest request){
        User user = getUser();
        Quiz quiz = quizService.getQuiz((UUID.fromString(quizId)));
        String filePath = quiz.getAttachment();
        File file = new File(filePath);

        String fileName=file.getName();
        //파일명 가져오기
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType(file);
        long fileLength = file.length();

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try(
                FileInputStream fis = new FileInputStream(filePath);
                OutputStream out = response.getOutputStream();
        ){
            int readCount;
            byte[] buffer = new byte[1024];

            while((readCount = fis.read(buffer))!= -1){
                out.write(buffer,0,readCount);
            }
        DownloadLog downloadLog = logService.buildDownloadLogByQuizAndUserIP(quiz, user, request.getRemoteAddr());
            logService.upsertDownload(downloadLog);
        }catch(Exception e){
            //여기도 logger 남겨야 하나
            throw new RuntimeException("file Download Error");
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
