package com.example.wechatlogin.demo.mapper;

import com.example.wechatlogin.demo.entity.Wechatuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mine
 * @since 2020-12-16
 */
public interface WechatuserMapper extends BaseMapper<Wechatuser> {

    Wechatuser findUserByWxOpenid(String openid);
}
