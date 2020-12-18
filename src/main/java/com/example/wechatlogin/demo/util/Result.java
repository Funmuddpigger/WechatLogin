package com.example.wechatlogin.demo.util;

import lombok.Data;

import java.io.Serializable;


@Data
public class Result implements Serializable {

    private Integer code; //状态码
    private String msg; //消息
    private Object data; //数据对象

    public void setData(Object data) {
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    /**
     *  成功状态
     * @param data
     * @return
     */
    public static Result success(Object data){
        Result result = new Result(); //别拿出去方法外面new,tm占用内存
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    /**
     * @Override
     * @param data
     * @param msg
     * @return
     */
    public static  Result success(String msg,Object data){
        Result result = new Result(); //别拿出去方法外面new,tm占用内存
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     *  失败状态
     *  请求失败,500是服务器问题,我们内部问题
     * @return
     */
    public static Result fail(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }


}
