package com.hck.zhuanqian.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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

    
}
