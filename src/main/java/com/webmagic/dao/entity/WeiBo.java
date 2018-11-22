package com.webmagic.dao.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class WeiBo implements Serializable {
    @Id
    private String id;
    private String userid;
    private Object mblogs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Object getMblogs() {
        return mblogs;
    }

    public void setMblogs(Object mblogs) {
        this.mblogs = mblogs;
    }

    public WeiBo(String userid, Object mblogs) {
        this.userid = userid;
        this.mblogs = mblogs;
    }
}
