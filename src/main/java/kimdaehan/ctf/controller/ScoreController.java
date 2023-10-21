package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import kimdaehan.ctf.auth.AuthenticationFacade;
import kimdaehan.ctf.dto.RankGraphDTO;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.RankService;
import kimdaehan.ctf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ScoreController extends  BaseController{

    private final RankService rankService;

    @Autowired
    public ScoreController(UserService userService, AuthenticationFacade authenticationFacade, RankService rankService) {
        super(userService, authenticationFacade);
        this.rankService = rankService;
    }

    @GetMapping({"/score"})
    public ModelAndView scoreMain(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName(("/score/score"));
        mv.addObject("title", "ScoreBoard");
        return mv;
    }

    @GetMapping({"/rank-graph/{affiliation}"})
    @ResponseBody
    public ResponseEntity<?> rankGraph(HttpServletRequest request, @PathVariable("affiliation") String affiliation){
        User user = getUser();
        logger.info("Try access rank-graph -> user : {}, ip : {}", user.getUserId(), request.getRemoteAddr());
        List<RankGraphDTO> rankGraphDTOList;
        if(affiliation.equals("YB") || affiliation.equals("NB")){
            rankGraphDTOList = rankService.getRankGraphByAffiliation(affiliation);
        }  else {
            return ResponseEntity.badRequest().body("Validation error");
        }
        return ResponseEntity.ok(rankGraphDTOList);
    }
}
