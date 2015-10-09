package com.hck.zhuanqian.data;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.TGUserBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TGUserData implements Serializable{
    @JsonProperty("tgUsers")
    private List<TGUserBean> tgUserDatas;

    public List<TGUserBean> getTgUserDatas() {
        return tgUserDatas;
    }

    public void setTgUserDatas(List<TGUserBean> tgUserDatas) {
        this.tgUserDatas = tgUserDatas;
    }

}
