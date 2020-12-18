package com.example.wechatlogin.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WechatToken implements Serializable {

    private String token;

    public WechatToken(String token) {
        this.token = token;
    }
}
