package com.example.wechatlogin.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wechatlogin.demo.entity.WechatToken;
import com.example.wechatlogin.demo.entity.Wechatuser;
import com.example.wechatlogin.demo.jwt.JwtUtils;
import com.example.wechatlogin.demo.mapper.WechatuserMapper;
import com.example.wechatlogin.demo.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wechatlogin.demo.vo.Code2SessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.AuthenticationException;
import java.net.URI;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mine
 * @since 2020-12-16
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class LoginServiceImpl extends ServiceImpl<WechatuserMapper, Wechatuser> implements LoginService {

    @Value("${wxcode.app.appid}")
    private String appid;

    @Value("${wxcode.app.appsecret}")
    private String secret;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private WechatuserMapper wechatuserMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public WechatToken loginin(String code) throws AuthenticationException {
        //code2Session获取
        String code2Session = code2Session(code);
        Code2SessionResponse code2SessionResponse = JSONObject.parseObject(code2Session, Code2SessionResponse.class);
        System.out.println(code2SessionResponse);
        if(!code2SessionResponse.getErrcode().equals("0")){
                throw new AuthenticationException("code2Session无效"+code2SessionResponse.getErrmsg());
        }else{
            Wechatuser wechatUser = wechatuserMapper.findUserByWxOpenid(code2SessionResponse.getOpenid());
            if (wechatUser == null) {
                wechatUser = new Wechatuser();
                wechatUser.setOpenid(code2SessionResponse.getOpenid());
                wechatuserMapper.insert(wechatUser);//不存在就新建用户
            }
            //4 . 更新sessionKey
            wechatUser.setSessionkey(code2SessionResponse.getSession_key());
            QueryWrapper<Wechatuser> objectWrapper = new QueryWrapper<>();
            objectWrapper.eq("openid",code2SessionResponse.getOpenid());
            wechatuserMapper.update(wechatUser,objectWrapper);
            System.out.println("session key: "+ code2SessionResponse.getSession_key());
            //5 . JWT 返回自定义登陆态 Token
            String token = jwtUtils.createToken(wechatUser);
            System.out.println(token);
            return new WechatToken(token);
        }
    }

    @Override
    public Wechatuser updateWechatUser(String openid) {
        return null;
    }

//    @Override
//    public Wechatuser updateWechatUser(String openid) {
//
//    }


    private String code2Session(String jscode){
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appid);
        params.add("secret", secret);
        params.add("js_code", jscode);
        params.add("grant_type", "authorization_code");
        URI code2Session = getURIwithParams(code2SessionUrl, params);
        System.out.println();
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
    }

    private URI getURIwithParams(String url, MultiValueMap<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        return builder.build().encode().toUri();
    }
}
