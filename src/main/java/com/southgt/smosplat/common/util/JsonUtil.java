package com.southgt.smosplat.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
	private static ObjectMapper objectMapper=null;
	/**
	 * 获得ObjectMapper单实例，除非明确制定新建一个
	 * @param createNew
	 * @return
	 */
	public static synchronized ObjectMapper getMapperInstance(boolean createNew){
		if(createNew){
			return new ObjectMapper();
		}else if(objectMapper==null){
			objectMapper=new ObjectMapper();
		}
		return objectMapper;
	}
	/**
	 * 将对象转为json字符串，包含所有属性
	 * @param obj
	 * @return
	 */
	public static String beanToJson(Object obj){
		ObjectMapper mapper = getMapperInstance(false); 
        try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 将对象转为json字符串，根据参数指定的条件进行属性过滤
	 * @param obj
	 * @param entityClass 实体类类对象
	 * @param filterClass 过滤类类对象
	 * @param filterId 过滤类注解名
	 * @param filterFields 要输出的属性
	 * @return
	 */
	public static String beanToJson(Object obj,Class<?> entityClass,Class<?> filterClass,String filterId,String[] filterFields){
		ObjectMapper mapper = getMapperInstance(false); 
		FilterProvider filterProvider=new SimpleFilterProvider()
				.addFilter(filterId, SimpleBeanPropertyFilter.filterOutAllExcept(filterFields));
		mapper.setFilterProvider(filterProvider);
		mapper.addMixIn(entityClass, filterClass);
        try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public static Object jsonToBean(String json, Class<?> cls){   
	    Object vo=null;
		try {
			vo = getMapperInstance(false).readValue(json, cls);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	    return vo;   
	} 
	
	/**
	 * 
	 * 获取泛型的Collection Type  
	 * @date  2017年8月4日 下午2:31:30
	 * 
	 * @param mapper
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 * JavaType
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月4日     姚家俊      v1.0          create</p>
	 *
	 */
     public static JavaType getCollectionType(ObjectMapper mapper,Class<?> collectionClass, Class<?>... elementClasses) {   
         return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
     }   
     
     /**
      * 
      * JSON 转换为 List
      * @date  2017年8月4日 下午2:30:58
      * 
      * @param json
      * @param cls
      * @return
      * @throws JsonParseException
      * @throws JsonMappingException
      * @throws IOException
      * List<?>
      * @throws null
      * 
      * @version v1.0
      * @author  姚家俊
      * <p>Modification History:</p>
      * <p>Date         Author      Version     Description</p>
      * <p> -----------------------------------------------------------------</p>
      * <p>2017年8月4日     姚家俊      v1.0          create</p>
      *
      */
 	public static <T> List<T> jsonToList(String jsonVal, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory t = TypeFactory.defaultInstance();
		// 指定容器结构和类型（这里是ArrayList和clazz）
		List<T> list = mapper.readValue(jsonVal, t.constructCollectionType(ArrayList.class, clazz));
		// 如果T确定的情况下可以使用下面的方法：
		// List<T> list = objectMapper.readValue(jsonVal, new
		// TypeReference<List<T>>() {});
		return list;
	}
}
