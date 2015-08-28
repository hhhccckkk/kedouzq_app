package com.hck.zhuanqian.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.KindBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class KindData {
    @JsonProperty("kinds")
	private List<KindBean> kindBeans;

	public List<KindBean> getKindBeans() {
		return kindBeans;
	}

	public void setKindBeans(List<KindBean> kindBeans) {
		this.kindBeans = kindBeans;
	}
    

}
