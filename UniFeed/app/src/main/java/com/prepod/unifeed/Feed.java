package com.prepod.unifeed;

/**
 * Created by Антон on 10.06.2016.
 */
public class Feed {

    private String id;
    private String createdTime;
    private String stroy;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setStroy(String stroy) {
        this.stroy = stroy;
    }

    public String getId() {
        return id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getStroy() {
        return stroy;
    }
}
