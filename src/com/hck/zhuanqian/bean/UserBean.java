package com.hck.zhuanqian.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserBean {
	@JsonProperty("id")
	private long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("mac")
	private String mac;
	@JsonProperty("shangjia")
	private String shangjia;
	@JsonProperty("jhm")
	private String jhm;
	@JsonProperty("isok")
	private int isok;
	@JsonProperty("tg")
	private long tg;
	@JsonProperty("allMoney")
	private long allMoney;
	@JsonProperty("kedoubi")
	private long allKeDouBi;
	@JsonProperty("xinshou")
	private boolean isXinShou;
	@JsonProperty("shareqq")
	private boolean isShareQQ;
	@JsonProperty("yaoqingqq")
	private boolean isYaoQingQQ;
	@JsonProperty("sharexinlang")
	private boolean isShareXinLang;
	@JsonProperty("qq")
	private String qq;
	@JsonProperty("zhifubao")
	private String zhifubao;
	@JsonProperty("phone")
	private String phone;
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getZhifubao() {
		return zhifubao;
	}
	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isShareQQ() {
		return isShareQQ;
	}
	public void setShareQQ(boolean isShareQQ) {
		this.isShareQQ = isShareQQ;
	}
	public boolean isYaoQingQQ() {
		return isYaoQingQQ;
	}
	public void setYaoQingQQ(boolean isYaoQingQQ) {
		this.isYaoQingQQ = isYaoQingQQ;
	}
	public boolean isShareXinLang() {
		return isShareXinLang;
	}
	public void setShareXinLang(boolean isShareXinLang) {
		this.isShareXinLang = isShareXinLang;
	}
	public boolean isXinShou() {
		return isXinShou;
	}
	public void setXinShou(boolean isXinShou) {
		this.isXinShou = isXinShou;
	}
	public long getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(long allMoney) {
		this.allMoney = allMoney;
	}
	public long getAllKeDouBi() {
		return allKeDouBi;
	}
	public void setAllKeDouBi(long allKeDouBi) {
		this.allKeDouBi = allKeDouBi;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getShangjia() {
		return shangjia;
	}
	public void setShangjia(String shangjia) {
		this.shangjia = shangjia;
	}
	public String getJhm() {
		return jhm;
	}
	public void setJhm(String jhm) {
		this.jhm = jhm;
	}
	public int getIsok() {
		return isok;
	}
	public void setIsok(int isok) {
		this.isok = isok;
	}
	public long getTg() {
		return tg;
	}
	public void setTg(long tg) {
		this.tg = tg;
	}
}
