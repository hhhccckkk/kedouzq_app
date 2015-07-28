package com.hck.zhuanqian.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.TuiGBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TuiGuangData {
	@JsonProperty("tgs")
	private List<TuiGBean> tuiGBeans;

	public List<TuiGBean> getTuiGBeans() {
		return tuiGBeans;
	}

	public void setTuiGBeans(List<TuiGBean> tuiGBeans) {
		this.tuiGBeans = tuiGBeans;
	}
	
	

}
