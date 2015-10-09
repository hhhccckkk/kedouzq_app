package com.hck.zhuanqian.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hck.zhuanqian.bean.TGUserBean;
import com.hck.zhuanqian.data.TGUserData;

public class JsonUtils {
    private static ObjectMapper sObjectMapper;

    private static ObjectMapper getMapper() {
        if (sObjectMapper == null) {
            synchronized (JsonUtils.class) {
                if (sObjectMapper == null) {
                    sObjectMapper = new ObjectMapper();
                    sObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }
        return sObjectMapper;
    }

  
    public static <T> T parse(String json, Class<T> clasz) throws JsonParseException, JsonMappingException,
            IOException {
        return getMapper().readValue(json, clasz);
    }

  

    public static String toString(Object object) throws JsonGenerationException, JsonMappingException, IOException {
        return getMapper().writeValueAsString(object);
    }

    /**
     *  "tgUsers": [
        {
            "tg": 0,
            "userName": "漫步云端",
            "touxiang": "http://q.qlogo.cn/qqapp/1104810179/2F520F64DA607A82485E0083EBF35483/40",
            "qq": "",
            "userId": 50,
            "jishu": 2
        },
        {
            "tg": 1,
            "userName": "漫步云端",
            "touxiang": "http://q.qlogo.cn/qqapp/1104810179/2F520F64DA607A82485E0083EBF35483/40",
            "qq": "",
            "userId": 49,
            "jishu": 1
        }
    ],

     */
    private static JSONArray jArray;
    private static JSONObject jObject;
    public static void getTgUser(String data,TGUserData userData){
        List<TGUserBean> userBeans=new ArrayList<>();
        try {
            TGUserBean userBean=null;
            
            jArray = new JSONArray(data);
            for (int i = 0; i < jArray.length(); i++) {
                jObject=jArray.getJSONObject(i);
                userBean=new TGUserBean();
                userBean.setJishu(jObject.getInt("jishu"));
                userBean.setQq(jObject.getString("qq"));
                userBean.setTg(jObject.getInt("tg"));
                userBean.setTouxiang(jObject.getString("touxiang"));
                userBean.setUserName(jObject.getString("userName"));
                userBean.setUserId(jObject.getLong("userId"));
                userBeans.add(userBean);
            } 
        } catch (Exception e) {
            LogUtil.D("eee: "+e.toString());
        }
        userData.setTgUserDatas(userBeans);
        
    }
    
}
