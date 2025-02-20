package com.eureka.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eureka.common.ErrorCode;
import com.eureka.exception.BusinessException;
import com.eureka.model.domain.User;
import com.eureka.service.UserService;
import com.eureka.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.eureka.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Eureka
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 盐值
     */
    private static final String SALT = "Eureka";

    /**
     * 无效字符
     */
    private static final String INVALID_PATTERN = "[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?`~\\\\]";

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return id
     */
    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount,password,checkPassword, planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || password.length() < 8 || checkPassword.length() < 8 || planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "格式错误");
        }
        Matcher matcher = Pattern.compile(INVALID_PATTERN).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "格式错误");
        }
        if (!checkPassword.equals(password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不同");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>()
                .lambda().eq(User::getUserAccount, userAccount).eq(User::getPlanetCode, planetCode);
        long count = count(lambdaQueryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或星球编号重复");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 新增
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = save(user);
        if (!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新增失败");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param password    用户密码
     * @param request     Http请求
     * @return User
     */
    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码为空");
        }
        if (userAccount.length() < 4 || password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Matcher matcher = Pattern.compile(INVALID_PATTERN).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 用户查询
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda()
                .eq(User::getUserAccount, userAccount).eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if (user == null){
            log.info("user not exist");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 用户脱敏
        User safeUser = getSafeUser(user);
        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);
        return safeUser;
    }

    /**
     * 用户注销
     *
     * @param request Http请求
     * @return Integer
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户脱敏
     *
     * @param user 用户
     * @return safeUser 脱敏用户
     */
    @Override
    public User getSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setRole(user.getRole());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setPlanetCode(user.getPlanetCode());
        safeUser.setCreateTime(user.getCreateTime());
        return safeUser;
    }
}




