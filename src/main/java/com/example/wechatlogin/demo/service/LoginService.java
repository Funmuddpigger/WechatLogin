package com.example.wechatlogin.demo.service;

import com.example.wechatlogin.demo.entity.WechatToken;
import com.example.wechatlogin.demo.entity.Wechatuser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.naming.AuthenticationException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mine
 * @since 2020-12-16
 */
public interface LoginService{

    WechatToken loginin(String code) throws AuthenticationException;

    Wechatuser updateWechatUser(String openid);

}
