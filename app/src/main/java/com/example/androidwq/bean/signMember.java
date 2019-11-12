package com.example.androidwq.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 签到成员表
 */
public class signMember extends LitePalSupport {

    /**
     * 签到表id
     */
    private int id;

    /**
     * 群成员id
     */
    private int memberId;

    /**
     * 是否签到：0为否，1为是
     */
    private int hasSign;

    /**
     * 签到地点x
     */
    private double sign_x;

    /**
     * 签到地点y
     */
    private double sign_y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getHasSign() {
        return hasSign;
    }

    public void setHasSign(int hasSign) {
        this.hasSign = hasSign;
    }

    public double getSign_x() {
        return sign_x;
    }

    public void setSign_x(double sign_x) {
        this.sign_x = sign_x;
    }

    public double getSign_y() {
        return sign_y;
    }

    public void setSign_y(double sign_y) {
        this.sign_y = sign_y;
    }

    @Override
    public String toString(){
        String content = "signMember{"
                +"id="+getId()+","
                +"memberId="+getMemberId()+","
                +"hasSign="+getHasSign()+","
                +"sign_x="+getSign_x()+","
                +"sign_y="+getSign_y()
                +"}";
        return content;
    }
}
