package com.example.wechatlogin.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class WechatToken {

    private String token;

    public WechatToken(String token) {
        this.token = token;
    }
}
