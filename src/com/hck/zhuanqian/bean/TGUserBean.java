package com.hck.zhuanqian.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TGUserBean implements Serializable {
    @JsonProperty("userId")
    private long userId;
    @JsonProperty("touxiang")
    private String touxiang;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("qq")
    private String qq;
    @JsonProperty("tg")
    private long tg;
    @JsonProperty("jishu")
    private int jishu;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public long getTg() {
        return tg;
    }

    public void setTg(long tg) {
        this.tg = tg;
    }

    public int getJishu() {
        return jishu;
    }

    public void setJishu(int jishu) {
        this.jishu = jishu;
    }

}
