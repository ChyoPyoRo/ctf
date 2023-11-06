package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.*;
import kimdaehan.ctf.entity.Quiz;
import kimdaehan.ctf.entity.Solved;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.RankService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ScoreController extends  BaseController{

    private final RankService rankService;
    private final ServerSettingService serverSettingService;

    @Autowired
    public ScoreController(UserService userService, AuthenticationFacade authenticationFacade, RankService rankService, ServerSettingService serverSettingService) {
        super(userService, authenticationFacade);
        this.rankService = rankService;
        this.serverSettingService = serverSettingService;
    }

    @GetMapping({"/score"})
    public ModelAndView scoreMain(HttpServletRequest request){
        User user = getUser();
        ModelAndView mv = new ModelAndView();
        if(serverSettingService.getServerStart().isAfter(LocalDateTime.now()) && !user.getType().equals(User.Type.ADMIN)){
            logger.info("Try access challenge main before start-> user : {}, router : Get(/challenge)", user.getUserId());
            mv.setViewName("/error/access_before_start");
            return mv;
        }
        mv.setViewName(("/score/score"));
        mv.addObject("title", "ScoreBoard");
        mv.addObject("user", user.getUserId());
        mv.addObject("type", user.getType());
        return mv;
    }

    @GetMapping({"/rank-graph/{affiliation}"})
    @ResponseBody
    public ResponseEntity<?> rankGraph(HttpServletRequest request, @PathVariable("affiliation") String affiliation){
        User user = getUser();
        logger.info("Try access rank-graph -> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        List<RankGraphDTO> rankGraphDTOList;
        if(affiliation.equals("YB") || affiliation.equals("NB") || affiliation.equals("SCH") || affiliation.equals("ALL")){
            rankGraphDTOList = rankService.getRankGraphByAffiliation(affiliation);
        }  else {
            return ResponseEntity.badRequest().body("Validation error");
        }
        return ResponseEntity.ok(rankGraphDTOList);
    }
    @GetMapping({"/rank-graph-current/{affiliation}"})
    @ResponseBody
    public ResponseEntity<?> rankGraphCurrent(HttpServletRequest request, @PathVariable("affiliation") String affiliation){
        User user = getUser();
        logger.info("Try access rank-graph-current-> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        List<RankGraphCurrentDTO> userPageDTOList;
        if(affiliation.equals("YB") || affiliation.equals("NB") || affiliation.equals("SCH") ){
            userPageDTOList = userService.getRankAndScoreUsersByAffiliationTop5(affiliation);
        } else if(affiliation.equals("ALL")){
            userPageDTOList = userService.getAllRankAndScoreUsersByAffiliationTop5();
        }else {
            return ResponseEntity.badRequest().body("Validation error");
        }
        return ResponseEntity.ok(userPageDTOList);
    }

    @GetMapping({"/rank-graph-history/{userId}/{affiliation}"})
    @ResponseBody
    public ResponseEntity<?> rankGraphHistroy(HttpServletRequest request, @PathVariable("userId") String userId,@PathVariable("affiliation") String affiliation){
        User user = getUser();
        logger.info("Try access rank-graph-history -> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        return ResponseEntity.ok(rankService.getRankListByUser(userId, affiliation));
    }

    @GetMapping({"/rank-all/{affiliation}"})
    @ResponseBody
    public ResponseEntity<?> rankAllData(HttpServletRequest request, @PathVariable String affiliation){
        User user = getUser();
        logger.info("Try access rank-all-> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        List<RankAllDTO> userPageDTOList;
        if(affiliation.equals("YB") || affiliation.equals("NB") || affiliation.equals("SCH")){
            userPageDTOList = userService.findScoreUsersByAffiliationAndUserIdWithoutIsBan(affiliation);
        } else if (affiliation.equals("ALL")){
            userPageDTOList = userService.findScoreUsersWithAllAndUserIdWithoutIsBan();
        }else {
            return ResponseEntity.badRequest().body("Validation error");
        }
        return ResponseEntity.ok(userPageDTOList);
    }

    @GetMapping({"/rank/challenge/{challengId}/{pageNum}"})
    public ResponseEntity<?> rankForSingleChallenge(HttpServletRequest request, @PathVariable String challengId, @PathVariable Integer pageNum){
        User user = getUser();
        UUID quizId;
        logger.info("Try access single challenge Rank -> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        try{
            quizId = UUID.fromString(challengId);
        } catch (IllegalArgumentException e) {
            //wrongID
            logger.info("Attempting to submit an ID that is not in UUID format  -> user : {}, router : Get(/rank/challenge/)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        List<QuizRankDto> returnList;
        returnList = rankService.getRankListByChallenge(quizId);
        if(returnList == null){
            //not Exist ID
            logger.info("Submitted UUID of non-existent challenge   -> user : {}, router : Get(/rank/challenge/)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
//        테스트용 페이지 네이션
//        List<QuizRankDto> testList = new ArrayList<QuizRankDto>();
//        for(int i =0; i<120;i++){
//            testList.add(returnList.get(0));
//        }

        int start = (pageNum - 1) * 12;
        int end = pageNum * 12;

        if(returnList.size() == 0){
            QuizRankPaginationDto result = new QuizRankPaginationDto(returnList, 0);
            return ResponseEntity.ok(result);
        }
        else if(start >= returnList.size()){
            logger.info("pageNumber Over   -> user : {}, router : Get(/rank/challenge/)", user.getUserId());
            return ResponseEntity.badRequest().body("ValidationError");
        }
        if(end > returnList.size()){
            end = returnList.size();
        }
        List<QuizRankDto> pageList = returnList.subList(start, end);
        int pageCount = (int) Math.ceil((double) returnList.size() / 12);
        QuizRankPaginationDto result = new QuizRankPaginationDto(pageList, pageCount);
        return ResponseEntity.ok(result);
    }


}
