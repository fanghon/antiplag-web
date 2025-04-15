package cn.mooyyu.antiplagweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("detail")
public class DetailController {
    @RequestMapping("jplag")
    public String jplag(HttpServletRequest request) {
        String session = request.getRequestedSessionId();
//        return "redirect:/"+session+"/index.html";
        return "redirect:/" + session + "/index.html";
   }
}
