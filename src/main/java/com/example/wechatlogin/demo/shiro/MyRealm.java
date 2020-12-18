package com.example.wechatlogin.demo.shiro;

import com.example.wechatlogin.demo.entity.Wechatuser;
import com.example.wechatlogin.demo.jwt.JwtToken;
import com.example.wechatlogin.demo.jwt.JwtUtils;
import com.example.wechatlogin.demo.mapper.WechatuserMapper;
import com.example.wechatlogin.demo.util.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class MyRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private WechatuserMapper wechatuserMapper;

    public List<Realm> allRealm() {
        List<Realm> realmList = new LinkedList<>();
        AuthorizingRealm jwtRealm = jwtRealm();
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }

    private AuthorizingRealm jwtRealm() {
        AuthorizingRealm jwtRealm = new AuthorizingRealm() {

            @Override
            public boolean supports(AuthenticationToken token) {
                return token instanceof JwtToken;
            }
            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
                String token = principals.toString();
                System.out.println("token:"+token);
                String wxOpenIdByToken = jwtUtils.getWxOpenIdByToken(token);

                return new SimpleAuthorizationInfo();
            }
            /**
             * 校验 验证token逻辑
             */
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
                String jwtToken = (String) token.getCredentials();
                String wxOpenId = jwtUtils.getWxOpenIdByToken(jwtToken);
                String sessionKey = jwtUtils.getSessionKeyByToken(jwtToken);
                if (wxOpenId == null || wxOpenId.equals(""))
                    throw new AuthenticationException("user account not exits , please check your token");
                if (sessionKey == null || sessionKey.equals(""))
                    throw new AuthenticationException("sessionKey is invalid , please check your token");
                if (!jwtUtils.verify(jwtToken))
                    throw new AuthenticationException("token is invalid , please check your token");
                return new SimpleAuthenticationInfo(token, token, getName());
            }
        };
        jwtRealm.setCredentialsMatcher(credentialsMatcher());
        return jwtRealm;
    }

    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }
}
