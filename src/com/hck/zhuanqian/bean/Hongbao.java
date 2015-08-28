package com.hck.zhuanqian.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Hongbao {
	@JsonProperty("id")
	private long id;
	@JsonProperty("uid")
	private long uid;
	@JsonProperty("uName")
	private String uName;
	@JsonProperty("content")
	String content;
	@JsonProperty("time")
	private String time;
	@JsonProperty("point")
	private int point;
	@JsonProperty("isOpen")
	private int isOpen;
	@JsonProperty("isXiTong")
    private int isXiTong;
    
	public int getIsXiTong() {
        return isXiTong;
    }

    public void setIsXiTong(int isXiTong) {
        this.isXiTong = isXiTong;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

}
