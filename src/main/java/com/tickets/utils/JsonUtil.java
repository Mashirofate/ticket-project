package com.tickets.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JsonUtil {
    public static String formatObjectToJsonString(Object obj){
        if(null != obj) {
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                String json = mapper.writeValueAsString(obj);
                return json;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象序列号json字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String writeValue(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串反序列号成对象
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T readValue(String json, Class<T> clazz) throws Exception {
        return MAPPER.readValue(json, clazz);
    }

    /**
     * json格式：List<HashMap<String, String>>
     * @Title: readValue
     * @author  271249676@qq.com	奇积奇积变
     * @date    2017年4月19日 下午8:32:52
     * @param json
     * @return
     * @throws Exception
     */
    public static <T> T readValue(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<List<HashMap<String, String>>>() {
        });
    }

    /**
     * 直接转List<String>
     * @Author liyuanq
     * @Time 2019年4月10日下午5:05:00
     * @param json
     * @return
     * @throws Exception
     */
    public static <T> T readValue2List(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<List<String>>() {
        });
    }

    /**
     * 将字符串转set集合
     * @Author liyuanq
     * @Time 2019年4月10日下午6:29:03
     * @param json
     * @return
     * @throws Exception
     */
    public static <T> T readValue2Set(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<Set<String>>() {
        });
    }

    /**
     * json格式：List<HashMap<String, String>>
     * @Title: readValue
     * @author  271249676@qq.com	奇积奇积变
     * @date    2017年4月19日 下午8:32:52
     * @param json
     * @return
     * @throws Exception
     */
    public static <T> T readValue2ListMap(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<List<HashMap<String, String>>>() {
        });
    }

    public static <T> T readValue2ListObjectMap(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<List<HashMap<String, Object>>>() {
        });
    }

    /**
     * 转成hashmap
     * @Title: readValuetomap
     * @author  liyq
     * @date    2017年7月27日 下午4:27:33
     * @param json
     * @return
     * @throws Exception
     */
    public static <T> T readValuetomap(String json) throws Exception {
        return (T) MAPPER.readValue(json, new TypeReference<HashMap<String, String>>() {
        });
    }

    /**
     * json 格式：List<bean>
     * @Title: readValue2List
     * @author  271249676@qq.com	奇积奇积变
     * @date    2017年4月19日 下午8:40:04
     * @param json
     * @param clazz1 list
     * @param clazz2 内部bean类型
     * @return
     * @throws Exception
     */
    public static <T> T readValue2List(String json, Class<?> clazz1, Class<?> clazz2) throws Exception {
        return MAPPER.readValue(json, getCollectionType(clazz1, clazz2));
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            JavaType type = MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return type;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从json对象中解析List数据
     * @Title: decodeJson
     * @author  liyq
     * @date    2017年4月20日 上午10:54:25
     * @param json 客户端传过来的json对象
     * @param clazz	需要解析成的对象：Foo.class
     * @param listNodeName json字符串中list的节点名称
     * @return
     */
    public static List<?> decodeJson(String json, Class<?> clazz, String listNodeName) {
        List<?> list = null;
        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            JsonNode data = jsonNode.get(listNodeName);
            if ((data.isArray()) && (data.size() > 0)) {
                list = MAPPER.readValue(data.traverse(), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 直接将list字符串转换成list
     * @param json
     * @param clazz
     * @return
     */
    public static List<?> decodeJson(String json, Class<?> clazz) {
        List<?> list = null;
        try {
            JsonNode data = MAPPER.readTree(json);
            if ((data.isArray()) && (data.size() > 0)) {
                list = MAPPER.readValue(data.traverse(), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
