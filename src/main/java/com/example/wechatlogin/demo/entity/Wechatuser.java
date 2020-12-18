package com.example.wechatlogin.demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author mine
 * @since 2020-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Wechatuser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户电话
     */
    private String tel;
    /**
     * 微信传回openid
     */
    private String openid;
    /**
     * 微信传会sessionkey
     */
    private String sessionkey;

}
