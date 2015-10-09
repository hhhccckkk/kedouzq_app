package com.hck.zhuanqian.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.ChouJiangBean;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ChouJiangData {
    @JsonProperty("data")
    private List<ChouJiangBean> beans;

    public List<ChouJiangBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ChouJiangBean> beans) {
        this.beans = beans;
    }

}
