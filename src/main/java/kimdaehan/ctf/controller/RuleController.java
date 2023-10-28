package kimdaehan.ctf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kimdaehan.ctf.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RuleController {
    @GetMapping({"/rule"})
    public ModelAndView scoreMain(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/rule");
        mv.addObject("title", "Rules");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("JSESSIONID");
        if(user == null){
            mv.addObject("user", null);
            mv.addObject("type", null);
        } else {
            mv.addObject("user", user.getUserId());
            mv.addObject("type", user.getType());
        }

        return mv;
    }
}
