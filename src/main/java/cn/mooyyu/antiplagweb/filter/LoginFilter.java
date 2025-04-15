package cn.mooyyu.antiplagweb.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        if ("login".equals(request.getRequestURI().split("/")[2])) chain.doFilter(req, resp);
        else {
            if ((Boolean) request.getSession().getAttribute("isLogin")) chain.doFilter(req, resp);
            else resp.getWriter().println("error");
        }
    }

    public void init(FilterConfig config) {
    }
}