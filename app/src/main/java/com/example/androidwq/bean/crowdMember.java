package com.example.androidwq.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 群成员表
 */
public class crowdMember extends LitePalSupport {

    /**
     * 群组id
     */
    private int crowdId;

    /**
     * 群成员id
     */
    private int memberId;

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

    @Override
    public String toString(){
        String content = "crowdMember{"
                +"crowdId="+getCrowdId()+","
                +"memberId="+getMemberId()
                +"}";
        return content;
    }
}
