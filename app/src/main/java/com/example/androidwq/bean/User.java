package com.example.androidwq.bean;

import com.example.androidwq.utils.DateUtils;

import org.litepal.crud.LitePalSupport;


/**
 * 用户表
 */
public class User extends LitePalSupport {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户头像名
     */
    private String userHeadImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage() {
        this.userHeadImage = String.valueOf(DateUtils.getCurrentTime()) + "_" + String.valueOf(getId());
    }

    @Override
    public String toString(){
        String content = "User{"
                +"id="+getId()+","
                +"username="+getUsername()+","
                +"password="+getPassword()+","
                +"phoneNumber="+getPhoneNumber()+","
                +"userHeadImage="+getUserHeadImage()
                +"}";
        return content;
    }
}
