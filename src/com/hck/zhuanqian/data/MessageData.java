package com.hck.zhuanqian.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hck.zhuanqian.bean.MessageBean;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MessageData {
	@JsonProperty("message")	
private List<MessageBean> msgBeans;

public List<MessageBean> getMsgBeans() {
	return msgBeans;
}

public void setMsgBeans(List<MessageBean> msgBeans) {
	this.msgBeans = msgBeans;
}


}
