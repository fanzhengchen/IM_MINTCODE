package com.mintcode.launchrnetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.params.HttpParams;

import com.mintcode.launchr.util.TTJSONUtil;


/**Http请求的参数结构体
 * @author RobinLin
 *
 */
public class MTHttpParameters implements HttpParams{
	private Map<String,Object> _httpParams;
	
	public MTHttpParameters()
	{
		_httpParams = new HashMap<String,Object>();
	}
	
	@Override
	public HttpParams copy() {
		MTHttpParameters copy = new MTHttpParameters();
		return copy;
	}
	
	public int size()
	{
		return _httpParams.size();
	}

	@Override
	public boolean getBooleanParameter(String name, boolean defaultValue) {
		Object value = _httpParams.get(name);
		Boolean result = defaultValue;
		try {
			result = (Boolean)value;
		} catch (ClassCastException e) {
			result = defaultValue;
		} finally {
			result = result == null ? defaultValue : result;
		}
		return result;
	}

	@Override
	public double getDoubleParameter(String name, double defaultValue) {
		Object value = _httpParams.get(name);
		Double result = defaultValue;
		try {
			result = (Double)value;
		} catch (ClassCastException e) {
			result = defaultValue;
		} finally {
			result = result == null ? defaultValue : result;
		}
		return result;
	}

	@Override
	public int getIntParameter(String name, int defaultValue) {
		Object value = _httpParams.get(name);
		Integer result = defaultValue;
		try {
			result = (Integer)value;
		} catch (ClassCastException e) {
			result = defaultValue;
		} finally {
			result = result == null ? defaultValue : result;
		}
		return result;
	}

	@Override
	public long getLongParameter(String name, long defaultValue) {
		Object value = _httpParams.get(name);
		Long result = defaultValue;
		try {
			result = (Long)value;
		} catch (ClassCastException e) {
			result = defaultValue;
		} finally {
			result = result == null ? defaultValue : result;
		}
		return result;
	}

	@Override
	public Object getParameter(String name) {
		return _httpParams.get(name);
	}

	@Override
	public boolean isParameterFalse(String name) {
		Object value = _httpParams.get(name);
		Boolean result = null;
		try {
			result = (Boolean)value;
		} catch (ClassCastException e) {
			result = true;
		} 
		return result;
	}

	@Override
	public boolean isParameterTrue(String name) {
		Object value = _httpParams.get(name);
		Boolean result = null;
		try {
			result = (Boolean)value;
		} catch (ClassCastException e) {
			result = false;
		} 
		return result;
	}

	@Override
	public boolean removeParameter(String name) {
		return _httpParams.remove(name) != null;
	}

	@Override
	public HttpParams setBooleanParameter(String name, boolean value) {
		_httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setDoubleParameter(String name, double value) {
		_httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setIntParameter(String name, int value) {
		_httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setLongParameter(String name, long value) {
		_httpParams.put(name, value);
		return this;
	}

	@Override
	public HttpParams setParameter(String name, Object value) {
		_httpParams.put(name, value);
		return this;
	}
	
	public Set<String> getKeySet()
	{
		return _httpParams.keySet();
	}
	
	public String toJson()
	{
		return TTJSONUtil.convertObjToJson(_httpParams);
	}
	
	public void setMap(Map<String, Object> map){
		_httpParams.putAll(map);
	}
	
	public Map<String, Object> getMap(){
		return _httpParams;
	}
}