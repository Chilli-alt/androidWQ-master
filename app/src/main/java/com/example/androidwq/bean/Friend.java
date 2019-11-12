package com.example.androidwq.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 好友表
 */
public class Friend extends LitePalSupport {

    /**
     * 用户id
     */
    private int userId;

    /**
     * 朋友id
     */
    private int friendId;

    /**
     * 朋友的备注名
     */
    private String friendSubname;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendSubname() {
        return friendSubname;
    }

    public void setFriendSubname(String friendSubname) {
        this.friendSubname = friendSubname;
    }

    @Override
    public String toString() {
        String content = "Friend{"
                +"userId="+getUserId()+","
                +"friendId="+getFriendId()+","
                +"friendSubname="+getFriendSubname()
                +"}";
        return content;
    }
}
