package com.example.wechatlogin.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wechatlogin.demo.service.LoginService;
import com.example.wechatlogin.demo.util.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mine
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/wechatuser")
public class WechatuserController {

    @Autowired
    private LoginService loginService;

    /**
     * 微信小程序端用户登陆api
     * 返回给小程序端 自定义登陆态 token
     */
    @PostMapping("/api/wx/user/login")
    public Result wxAppletLoginApi(@RequestBody Map<String, String> request) throws AuthenticationException {
        System.out.println(request);
        if (!request.containsKey("code") || request.get("code") == null || request.get("code").equals("")) {
            return Result.fail(500, "失败");
        } else {
            return Result.success("success loginin",loginService.loginin(request.get("code")));
        }
    }

    @RequiresAuthentication
    @PostMapping("/sayHello")
    public Result sayHello() {
        Map<String, String> result = new HashMap<>();
        result.put("words", "hello World");
        return Result.success("hello", HttpStatus.OK);
    }

    @PostMapping("/update/wechatuser")
    public Result updateWechatUser(@RequestBody JSONObject  jsonObject) {
        QueryWrapper<Object> updateWrapper = new QueryWrapper<>();
        //updateWrapper.eq("openid",openid);
        return Result.success("hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/401", method = RequestMethod.GET)
    public Result error_401() {
        return Result.fail(401, "认证失败");
    }
}
