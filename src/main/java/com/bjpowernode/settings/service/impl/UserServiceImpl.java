package com.bjpowernode.settings.service.impl;

import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public User login(String loginAct ,String loginPwd ,String ip) throws LoginException {

        Map<String,String>map=new HashMap<String,String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
      //验证账号密码
        User user =userDao.login(map);
        if(user==null){
            throw new LoginException("账号密码错误");
        }
       //验证失效时间
        String expireTime=user.getExpireTime();//表格里的时间
        String currentTime = DateTimeUtil.getSysTime();//当前的时间
        if(expireTime.compareTo(currentTime)<0){//前小后大返回-1，表示表格里的时间小于当前时间
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState =user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        //判断ip地址
       String allowIps = user.getAllowIps();
       if(!allowIps.contains(ip)){
           throw new LoginException("ip受限");
       }

        return user;
    }

    @Override
    public List<User> getUserList() {
    List<User> users= userDao.getUserList();

        return users;
    }


}
