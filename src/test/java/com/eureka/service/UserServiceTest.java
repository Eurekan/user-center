package com.eureka.service;

import com.eureka.model.domain.User;
import com.eureka.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserServiceImpl userService;

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("Eureka");
        user.setUserAccount("Eureka");
        user.setAvatarUrl("https://www.baidu.com/link?url=G8JFT_Zqi5ViDDBCMd3gN0iUy5OQlh_iv7KsRNACMLoyLWqzB9r2TUeZRuj8CUjZBrFGd4x4cg2f3Qyrz2mO3FLbbN-N9cXXBadFy_-UuBIsuWwwTMI7MXGTRPH8SgOPJe8fMRnd3DZJNpq4NwuaH3wGGaIqMCCsNunWn9ZfWf-ZJuwE-uLQZBPAmCobkNi1fhZmE5MQ2O9PZx0kfVKjimkTauKheJDuM206AJ0kKNQqxtPLTk6pcsIrRsvKDWdk7GB_YUcqOS64SqThNVTgKvpztNmlSR5O-4i9u2UuZvzPYUzTMuJN43RaAFpyAQDncrrK3SapC3c9SIcIQCMgFu2m7IiViUGT0ENvvdRrW8z6hfTSQuji_G9qgMqlCefpokwG3enLSC7uBt2_KxhytDz-nmHMN-hpS0pQrotD2eOxyaaXFYkJ-PI-49flclaUhS-mbGGKqFdrZX38aq2lTO89qg1O2dPgat72TMrca8bk8DhGNTHCw0ZznAMhe4IFrHotB2-hbwQge65CJeX0k3zdACC18sDfLp2ZckDEs4TdYzBBaEnpvr9KCe8m0hTTfpBwWLwy9ZCL8lNbTeVdC-z7sP8TUTBVnwy-enok-nj5q2Rv5NqOQr7U9--7y5a7br22khQFWIk_6Mcezi_8Mcqa8Kd_7_e_yZhL2EXN_sy25Z7d6QdudYWKHV-UHTC_TYg58TuWdIMzWH9haRKpaK&wd=&eqid=87a0492a000227380000000367a8893c");
        user.setGender(1);
        user.setUserPassword(DigestUtils.md5DigestAsHex(("Eureka" + "12345678").getBytes()));
        user.setEmail("xxx@163.com");
        user.setPhone("19274716411");
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
        System.out.println(user.getId());
    }

    @Test
    void userRegister() {
        String userAccount = "Eureka";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "Eu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "Eureka";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "Eu ka";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "dogEureka";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "Eureka";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
    }
}