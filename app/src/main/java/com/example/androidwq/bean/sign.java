package com.example.androidwq.bean;

import com.example.androidwq.utils.DateUtils;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 签到表
 */
public class sign extends LitePalSupport {

    /**
     * 签到表id
     */
    private int id;

    /**
     * 群组id
     */
    private int crowdId;

    /**
     * 用户id
     */
    private int memberId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 目的坐标x
     */
    private double des_x;

    /**
     * 目的坐标y
     */
    private double des_y;

    /**
     * 有效范围
     */
    private int range;

    /**
     * 签到结果
     */
    private int signResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(int crowdId) {
        this.crowdId = crowdId;
    }


    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = DateUtils.getCurrentTime();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = DateUtils.getCurrentTime();
    }

    public double getDes_x() {
        return des_x;
    }

    public void setDes_x(double des_x) {
        this.des_x = des_x;
    }

    public double getDes_y() {
        return des_y;
    }

    public void setDes_y(double des_y) {
        this.des_y = des_y;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getSignResult() {
        return signResult;
    }

    public void setSignResult(int signResult) {
        this.signResult = signResult;
    }

    @Override
    public String toString(){
        String content = "sign{"
                +"id="+getId()+","
                +"crowdId="+getCrowdId()+","
                +"memberId="+getMemberId()+","
                +"startTime="+getStartTime()+","
                +"endTime="+getEndTime()+","
                +"des_x="+getDes_x()+","
                +"des_y="+getDes_y()+","
                +"range="+getRange()+","
                +"signResult="+getSignResult()
                +"}";
        return content;
    }
}
