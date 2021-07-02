package com.bjpowernode.settings.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.MD5Util;
import com.bjpowernode.utils.PrintJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")//所有请求地址的公共部分。叫做模块名称
public class UserController {
    @Autowired
    private UserService service;
    //返回值为void响应ajax请求
    @RequestMapping(value = "/login.do")
    public  void dologin(String loginAct,String loginPwd, HttpServletRequest request , HttpServletResponse response, HttpSession session) throws IOException {

        System.out.println("进入到验证登录操作...");
        String loginPwd1= MD5Util.getMD5(loginPwd); //密码转成密文形式
        String ip = request.getRemoteAddr();//接受浏览器端的ip地址
        System.out.println(ip+"---------------------");
        try{
            User user =service.login(loginAct,loginPwd1,ip);
            request.getSession().setAttribute("user",user);
           //执行到这表示没有抛出异常，登录成功
            PrintJson.printJsonFlag(response,true);//输出到前端
        }catch (Exception e){
            e.printStackTrace();
            //执行到这表示controller中抛出了异常
            String msg = e.getMessage();
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);//把map转成json格式输出到前端
        }

    }

}
