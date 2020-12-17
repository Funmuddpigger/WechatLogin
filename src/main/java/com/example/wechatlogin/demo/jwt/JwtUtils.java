package com.example.wechatlogin.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.wechatlogin.demo.entity.Wechatuser;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    private static final String CLAIM_NAME_OPEN = "openId";
    private static final String CLAIM_NAME_SESSION = "sessionKey";

    private static final String MY_SIGN = "asd48qw78wq445asd48q7w8eq4654d65as489a7d8wq46465da";



    public String createToken(Wechatuser wechatuser) {
        String jwtId = UUID.randomUUID().toString();
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            //加密
            Algorithm algorithm = Algorithm.HMAC256(MY_SIGN);
            return JWT.create()
                    .withClaim(CLAIM_NAME_OPEN, wechatuser.getOpenid())
                    .withClaim(CLAIM_NAME_SESSION,wechatuser.getSessionkey())
                    .withClaim("jwt_id",jwtId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(MY_SIGN);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withClaim(CLAIM_NAME_OPEN, getWxOpenIdByToken(token))
                    .withClaim(CLAIM_NAME_SESSION,getSessionKeyByToken(token))
                    .withClaim("jwt_id",getJwtIdByToken(token))
                    .acceptExpiresAt(EXPIRE_TIME)
                    .build();

            jwtVerifier.verify(token);

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 根据Token获取wxOpenId
     */
    public String getWxOpenIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(CLAIM_NAME_OPEN).asString();
    }

    /**
     * 根据Token获取sessionKey
     */
    public String getSessionKeyByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(CLAIM_NAME_SESSION).asString();
    }

    /**
     * 根据Token 获取jwt-id
     */
    private String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt_id").asString();
    }
}
