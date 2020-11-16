package com.example.demo.common.config;

import com.example.demo.common.conf.DisplayErrorCode;
import io.swagger.annotations.ApiModelProperty;

public class RestfulEntity<T> {
    /**
     * 接口调用返回状态码，默认为0(正常)，
     * 当调用出错时,默认为1(异常)，可自行填写对应错误码
     */
    @ApiModelProperty(value = "返回状态", required = true)
    private String status;



    /**
     * 接口调用返回数据
     */
    @ApiModelProperty(value = "返回数据", required = true)
    private T res;

    /**
     * 调用成功或者失败都可以设置消息
     */
    @ApiModelProperty(value = "接口调用信息", required = true)
    private String msg;


    public RestfulEntity(String status, T res) {
        this.status = status;
        this.res = res;
    }

    public RestfulEntity(String status,  T res, String msg) {
        this.status = status;
        this.res = res;
        this.msg = msg;
    }

    public RestfulEntity(String status,  String msg) {
        this.status = status;
        this.msg = msg;
    }

    public RestfulEntity( DisplayErrorCode code, T res) {
        this.status = String.valueOf(code.getVal());
        this.msg = code.getMessage();
        this.res = res;
    }

    /**
     * 返回异常请求时调用，须重新登录
     *
     * @return
     */
    public static <T> RestfulEntity<T> getAuthFailure(DisplayErrorCode errorCode) {
        return new RestfulEntity<>(errorCode, null);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     *
     * @param res
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(T res) {
        return new RestfulEntity<>("1", res);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     * 自定义失败消息
     *
     * @param res
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(T res, String msg) {
        return new RestfulEntity<>("1", res, msg);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     * 兼容平台PcoErrorCode
     *
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(DisplayErrorCode errorCode) {
        return new RestfulEntity<>( errorCode, null);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     * 兼容平台PcoErrorCode
     *
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(Integer errorCode, String msg) {
        return new RestfulEntity<>(String.valueOf(errorCode),  null, msg);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     * 兼容平台PcoErrorCode
     *
     * @param errorCode
     * @param res
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(DisplayErrorCode errorCode, T res) {
        return new RestfulEntity<>( errorCode, res);
    }

    /**
     * 返回正常请求时调用
     *
     * @param res
     * @return
     */
    public static <T> RestfulEntity<T> getSuccess(T res) {
        return new RestfulEntity<>("0",  res);
    }

    /**
     * 返回正常请求时调用
     * 自定义成功消息
     * @param res
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getSuccess(T res, String msg) {
        return new RestfulEntity<>("0",  res, msg);
    }

    public static <T> RestfulEntity<T> getSuccess(String msg) {
        return new RestfulEntity<>("0",  msg);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
