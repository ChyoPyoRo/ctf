package kimdaehan.ctf.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.*;
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
import kimdaehan.ctf.util.HttpReqRespUtils;
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
import java.util.*;


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
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(serverSettingService.getServerStart().isAfter(LocalDateTime.now()) && !user.getType().equals(User.Type.ADMIN)){
            logger.info("Try access challenge main before start-> user : {}, router : Get(/challenge)", user.getUserId());
            mv.setViewName("error/access_before_start");
            return mv;
        }
        //메인 페이지
        mv.setViewName("quiz/quiz_main");
        ArrayList <String> categoryList = new ArrayList<>(Arrays.asList("REVERSING", "PWN", "WEB", "FORENSICS", "MISC"));
        List<Solved> solvedList = quizService.getSolvedListByUserId(user.getUserId());
        for(String item : categoryList){
            Quiz.CategoryType categoryName = Quiz.CategoryType.valueOf(item);
            List<QuizListDTO> quizList = quizService.findQuizAfterStartTime(categoryName);
            List<QuizMainListDTO> quizGetDTOS = new ArrayList<>();
            for(QuizListDTO quiz : quizList){
                boolean isSolved = solvedList.stream().anyMatch(solved -> solved.getSolved().getQuizId().equals(quiz.getQuizIdAsUuid()));
                quizGetDTOS.add(QuizMainListDTO.from(quiz, isSolved));
            }
            mv.addObject(item, quizGetDTOS); // 수정된 부분
        }
        mv.addObject("user", user.getUserId());
        mv.addObject("type", user.getType());
        mv.addObject("title", "Challenge");

        return mv;
    }

    @GetMapping({"/quiz/{quizId}"})
    public ResponseEntity<?> quizListByCategory(@PathVariable String quizId,  HttpServletRequest request){
        User user = getUser();
        UUID uuid;
        try {
            uuid = UUID.fromString(quizId);
        } catch (IllegalArgumentException e) {
            //UUID가 아닌 형식으로 요청
            logger.info("Attempting to submit an ID that is not in UUID format  -> user : {}, router :  Get(quiz/{quizId})", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        Quiz quizDetail = quizService.getQuiz(uuid);
        if(quizDetail == null){
            //존재하지 않는 요청
            logger.info("Submitted UUID of non-existent challenge  -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        //start시간 제출이 안되면
        if(quizDetail.getStartTime().isAfter(LocalDate.now().atTime(LocalTime.now()))){
            //logger 생성
            logger.info("Access try before start time -> user : {}, router : Get(quiz/{quizId})", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        //로그 기록
        AccessLog accessLog = logService.buildAccessLogByQuizAndUserAndIP(quizDetail, user, HttpReqRespUtils.getClientIpAddressIfServletRequestExist());
        logService.upsertAccess(accessLog);
        QuizOneDto quizOne = QuizOneDto.from(quizDetail);
        return ResponseEntity.ok(quizOne);
    }

    @PostMapping({"/challenge/{challengeId}"})
    @ResponseBody
    public ResponseEntity<String> solveQuiz(@PathVariable String challengeId, QuizAnswerDto answer, HttpServletRequest request) {
        //user정보, quiz 정보 가져오기
        User user = getUser();
        UUID quizId;
        //flag값이 비어있음
        if(answer.getFlag() == null){
            logger.info("Attempting to submit Empty Flag  -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("emptyFlag");
        }
        //id의 형식이 uuid가 맞는지
        try {
            quizId = UUID.fromString(challengeId);
        } catch (IllegalArgumentException e) {
            //wrongID
            logger.info("Attempting to submit an ID that is not in UUID format  -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        Quiz quiz = quizService.getQuiz(quizId);
        //quiz값이 없을 경우 (존재하지 않는 ID)
        if(quiz == null){
            //not Exist ID
            logger.info("Submitted UUID of non-existent challenge  -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        //로그 조회 -> uuid의 형식이 아니면 로그 조회가 불가능
        List<FlagLog> isBruteForce = logService.getFlagLogByUserOneMinuteAgo(quiz,user);
        if(isBruteForce.size() > 5){
            //같은 문제에 대해서 5번 이상 요청이 들어왔었으면
            logger.info("Request at least 5 times in 1 minute  -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("TooManyRequest");
        }
        if(serverSettingService.getServerEnd().isBefore( LocalDate.now().atTime(LocalTime.now()))  ){
            //시간이 지났는데 제출 시도, logger 남겨야 되나
            logger.info("Try submitting after ending time -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
            return ResponseEntity.badRequest().body("ctfFinish");
        }
        //2.이 문제의 start time이 현재시간 이후이면 안됨 >> get으로 옮김
        //flag값 비교
        if (quiz.getFlag().equals(answer.getFlag())) {
            //맞췄을 시에 이미 정답을 맞췄으면 에러 발생
            Solved isSolvedBefore = quizService.findBySolvedAndSolvedIdSolvedUserId(user.getUserId(), quiz);
            if(isSolvedBefore!=null){
                logger.info("Try submitting already correct challenge -> user : {}, router : Post(challenge/{challengeID})", user.getUserId());
                return ResponseEntity.badRequest().body("correctAlready");
            }
            //OB, ADMIN, isBan 3개는 점수 추가 안하고 로그만 남기기
            //점수 추가
            if(!user.getType().equals(User.Type.ADMIN) && user.getIsBan().equals(User.IsBan.DISABLE) && !user.getAffiliation().equals(User.Affiliation.OB)){
                //ADMIN이 아니면 AND isBan 당하지 않았으면 AND OB가 아니면
                Solved solved = Solved.builder()
                        .solvedId(new SolvedId(quiz.getQuizId(), user.getUserId()))
                        .build();
                solved.setSolved(quiz);
                //solved 저장
                quizService.upsertSolvedQuiz(solved);
                //저장 후 점수 계산 및 quiz Table Score 수정
                DynamicScoreDTO scoreInfo =quizService.getDynamicScoreByQuizId(quizId);
                quizService.saveQuizScore(quiz, scoreInfo);
            }
            //로그 남기기
            FlagLog.SuccessOrNot successOrNot = FlagLog.SuccessOrNot.SUCCESS;
            FlagLog flagLog = logService.buildFlagLogByQuizAndUserIPAndSuccessFail(quiz, user, HttpReqRespUtils.getClientIpAddressIfServletRequestExist(),answer.getFlag(), successOrNot);
            logService.upsertFlag(flagLog);
            //user의 currentSolvedDateTime수정
            userService.changeUserCurrentSolvedDateTime(user);
            return ResponseEntity.ok("Correct");
        } else {
            //일치하지 않으면
            //로그만
            FlagLog.SuccessOrNot successOrNot = FlagLog.SuccessOrNot.FAIL;
            FlagLog flagLog = logService.buildFlagLogByQuizAndUserIPAndSuccessFail(quiz, user, HttpReqRespUtils.getClientIpAddressIfServletRequestExist(),answer.getFlag(), successOrNot);
            logService.upsertFlag(flagLog);
            return ResponseEntity.ok("Wrong");
        }
    }
    @GetMapping({"/quiz/download/{challengId}"})
    public ResponseEntity<?> download(HttpServletResponse response, @PathVariable String challengId, HttpServletRequest request){
        User user = getUser();
        UUID quizId;
        try {
            quizId = UUID.fromString(challengId);
        } catch (IllegalArgumentException e) {
            //wrongID
            logger.info("Attempting to submit an ID that is not in UUID format  -> user : {}, router : Get(quiz/download)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        Quiz quiz = quizService.getQuiz(quizId);
        if(quiz == null){
            //not Exist ID
            logger.info("Submitted UUID of non-existent challenge   -> user : {}, router : Get(quiz/download)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        if(quiz.getAttachment() == null){
            //파일이 없는 ID에 요청
            logger.info("Try to download to a challenge that does not have a file  -> user : {}, router : Get(quiz/download)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
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
            DownloadLog downloadLog = logService.buildDownloadLogByQuizAndUserIP(quiz, user, HttpReqRespUtils.getClientIpAddressIfServletRequestExist());
            logService.upsertDownload(downloadLog);
            return ResponseEntity.ok().body("DownloadSuccess");
        }catch(Exception e){
            //여기도 logger 남겨야 하나
            logger.info("file download error  -> user : {}", user.getUserId());
            throw new RuntimeException("file Download Error");
        }
    }




//    @GetMapping({"/test/{quizId}"})
//    public ResponseEntity<?> test(@PathVariable String quizId){
//        User user = getUser();
//        Quiz quiz = quizService.getQuiz(UUID.fromString(quizId));
//        Solved solved = Solved.builder()
//                .solvedId(new SolvedId(quiz.getQuizId(), user.getUserId()))
//                .build();
//        solved.setSolved(quiz);
//        quizService.upsertSolvedQuiz(solved);
//        return ResponseEntity.ok("good");
//    }
//
//
//
//    @GetMapping({"/test2/{quizId}"})
//    public ResponseEntity<?> test2(@PathVariable String quizId){
//        User user = getUser();
//        DynamicScoreDTO dynamicScoreDTO = quizService.getDynamicScoreByQuizId(UUID.fromString(quizId));
//        return ResponseEntity.ok(dynamicScoreDTO);
//    }
}
