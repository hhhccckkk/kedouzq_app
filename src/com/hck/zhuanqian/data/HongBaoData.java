package com.hck.zhuanqian.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.Hongbao;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HongBaoData {
	@JsonProperty("hongbaos")
	private List<Hongbao> hongbao;

	public List<Hongbao> getHongbao() {
		return hongbao;
	}

	public void setHongbao(List<Hongbao> hongbao) {
		this.hongbao = hongbao;
	}
	
	

}
