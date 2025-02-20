package com.eureka.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eureka.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Eureka
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return id
     */
    long userRegister(String userAccount, String password, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param password    用户密码
     * @param request     Http请求
     * @return User
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);


    /**
     * 用户脱敏
     *
     * @param user 用户
     * @return safeUser 脱敏用户
     */
    User getSafeUser(User user);

    /**
     * 用户注销
     *
     * @param request Http请求
     * @return Integer
     */
    int userLogout(HttpServletRequest request);
}
