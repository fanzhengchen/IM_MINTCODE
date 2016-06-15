package com.mintcode.chat.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mintcode.DataPOJO;

public class JsonUtil {
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
	
	public static String convertObjToJson(Object obj)
	{
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
	
	public static<T> T convertJsonToObj(String content,TypeReference<T> valueType) {
		T retValue = null;
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
	
	public static DataPOJO convertJsonToData(String content) {
		DataPOJO retValue = null;
		try {
			retValue = mMapper.readValue(content, DataPOJO.class);
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
