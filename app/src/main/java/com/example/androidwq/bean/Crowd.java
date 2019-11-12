package com.example.androidwq.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 群组表
 */
public class Crowd extends LitePalSupport {

    /**
     * 群组id
     */
    private int crowdId;

    /**
     * 群组名
     */
    private String crowdName;

    /**
     * 群组的群主id
     */
    private int crowdOwnerId;

    public int getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(int crowdId) {
        this.crowdId = crowdId;
    }

    public String getCrowdName() {
        return crowdName;
    }

    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }

    public int getCrowdOwnerId() {
        return crowdOwnerId;
    }

    public void setCrowdOwnerId(int crowdOwnerId) {
        this.crowdOwnerId = crowdOwnerId;
    }


    @Override
    public String toString() {
        String content = "Crowd"
                +"crowdId="+getCrowdId()+","
                +"crowdName="+getCrowdName()+","
                +"crowdOwnerId="+getCrowdOwnerId()
                +"}";
        return content;
    }
}
