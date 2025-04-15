package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginService {
    private LoginDao loginDao;
    @Autowired
    public LoginService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public boolean login(String name, String password, HttpServletRequest request) {
//        if (name.equals("test") && password.equals("beacon")) return true;
        if (password != null && password.equals(loginDao.getPwdByLoginName(name))) {
            request.getSession().setAttribute("isLogin", true);
            return true;
        }
        return false;
    }

    public boolean isLogin(HttpServletRequest request) {
        Object isLogin = request.getSession().getAttribute("isLogin");
        return isLogin != null && isLogin.equals(true);
    }

    public void exit(HttpServletRequest request) {
        request.getSession().setAttribute("isLogin", false);
    }
}
