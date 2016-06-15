package com.mintcode.launchr.util;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The tools of parse the valueType to Json 
 * @author MiyaJiang
 *
 */
public class TTJSONUtil {
	
private static ObjectMapper mMapper = new ObjectMapper();
	
	static {
		mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mMapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
		mMapper.configure(Feature.ALLOW_COMMENTS, true);
		mMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mMapper.setSerializationInclusion(Include.NON_NULL);
		mMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	/**将结构体对象转成Json string.
	 * @param obj
	 * @return
	 */
	public static String convertObjToJson(Object obj)
	{
		//下面这句做什么的，有点忘记了——@RobinLin
//		mMapper.disableDefaultTyping();
		String json = null;
		try {
			json = mMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static<T> T convertJsonToCommonObj(String content,Class<T> valueType) {
		T retValue = null;
//		mMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
		try {
			retValue = mMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	/**将json的string转成对应的Map或者结构体
	 * @param content
	 * @param valueType
	 * @return
	 */
	public static<T> T convertJsonToObj(String content,TypeReference<T> valueType) {
		T retValue = null;
//		mMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
		try {

			retValue = mMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retValue;
	}
}
