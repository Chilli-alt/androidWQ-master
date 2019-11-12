package com.example.androidwq.bean;

import com.example.androidwq.utils.DateUtils;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 好友消息表
 */
public class FriendMessage extends LitePalSupport {

    /**
     * 发送人的id
     */
    private int senderId;

    /**
     * 接收人的id
     */
    private int receiverId;

    /**
     * 发送消息的内容
     */
    private String message;

    /**
     * 发送消息的时间
     */
    private Date sendTime;


    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
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
    public String toString() {
        String content = "FriendMessage{"
                +"senderId="+getSenderId()+","
                +"receiverId="+getReceiverId()+","
                +"message="+getMessage()+","
                +"sendTime="+getSendTime()
                +"}";
        return content;
    }
}
