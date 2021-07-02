package com.bjpowernode.web.filter;

import com.bjpowernode.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到过滤器");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String path = request.getServletPath();//取出当前访问的路径
        System.out.println(path);
        if("/login.jsp".equals(path)||"/user/login.do".equals(path)){//如果是登录页放行
            filterChain.doFilter(request,response);
        }else {
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");
            if(user!=null){
                filterChain.doFilter(request,response);
            }else {//如果没有user表示不是从登录页面进来的,重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }


    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}
