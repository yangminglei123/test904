package com.bjpowernode.settings.service;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.exception.LoginException;

import java.util.List;

public interface UserService {
      User login(String loginAct , String loginPwd , String ip) throws LoginException;

      List<User> getUserList();
}
