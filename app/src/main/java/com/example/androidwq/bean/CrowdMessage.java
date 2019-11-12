package com.example.androidwq.bean;

import com.example.androidwq.utils.DateUtils;

import java.util.Date;

/**
 * 群组消息表
 */
public class CrowdMessage {

    /**
     * 群组id
     */
    private int crowdId;

    /**
     * 发送人id
     */
    private int senderId;

    /**
     * 发送的消息内容
     */
    private String message;

    /**
     * 发送时间
     */
    private Date  sendTime;


    public int getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(int crowdId) {
        this.crowdId = crowdId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = DateUtils.getCurrentTime();
    }

    @Override
    public String toString(){
        String content = "CrowdMessage{"
                +"crowdId="+getCrowdId()+","
                +"senderId="+getSenderId()+","
                +"message="+getMessage()+","
                +"sendTime"+getSendTime()
                +"}";
        return content;
    }
}
