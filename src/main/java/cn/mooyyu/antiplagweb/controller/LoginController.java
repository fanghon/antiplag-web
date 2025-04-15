package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class LoginController {
    private LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("login")
    @ResponseBody
    public boolean login(@RequestParam String name, @RequestParam String password, HttpServletRequest request) {
        Logger.getLogger(this.getClass().getName()).info(name+"is login in System");
        return loginService.login(name, password, request);
    }

    @GetMapping("isLogin")
    @ResponseBody
    public boolean isLogin(HttpServletRequest request) {
        return loginService.isLogin(request);
    }

    @GetMapping("exit")
    @ResponseBody
    public void exit(HttpServletRequest request) {
        loginService.exit(request);
    }
}
