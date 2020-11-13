package com.example.demo.entity;

import java.util.Date;

//数据库中保存的用户数据
public class User {
    private String userId;
    private String tenant;
    private String userName;
    private String password;
    private String token;
    private Date landTime;
    private Integer authority;
    private String belongTo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLandTime() {
        return landTime;
    }

    public void setLandTime(Date landTime) {
        this.landTime = landTime;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", tenant='" + tenant + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", landTime=" + landTime +
                ", authority='" + authority + '\'' +
                ", belongTo='" + belongTo + '\'' +
                '}';
    }
}
