package com.hck.zhuanqian.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Config {
	@JsonProperty("config1")
	private int config1;
	@JsonProperty("config2")
	private int config2;
	@JsonProperty("config3")
	private int config3;
	@JsonProperty("image1")
	private String config4;
	@JsonProperty("image2")
	private String config5;

	public int getConfig1() {
		return config1;
	}

	public void setConfig1(int config1) {
		this.config1 = config1;
	}

	public int getConfig2() {
		return config2;
	}

	public void setConfig2(int config2) {
		this.config2 = config2;
	}

	public int getConfig3() {
		return config3;
	}

	public void setConfig3(int config3) {
		this.config3 = config3;
	}

	public String getConfig4() {
		return config4;
	}

	public void setConfig4(String config4) {
		this.config4 = config4;
	}

	public String getConfig5() {
		return config5;
	}

	public void setConfig5(String config5) {
		this.config5 = config5;
	}

}
