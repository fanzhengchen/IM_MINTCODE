package com.mintcode.launchrnetwork;

import org.xml.sax.helpers.DefaultHandler;



/**处理json的BeanFactory类
 * @author RobinLin
 *
 */
public class MTBeanFactory extends DefaultHandler{

	/**将字符串格式的json转成对应的结构体
	 * @param jsonData 字符串json
	 * @return 在{@link BasePOJO}里登记的结构体,如果失败，则返回jsonData字符串
	 */
	public static Object getBean(String jsonData) {
		Object bean = null;
//		if(jsonData == null || jsonData.equals("") ) return bean;
//		bean = JsonUtil.convertJsonToObj(jsonData, new TypeReference<BasePOJO>() {});
//		if(bean == null){
//			bean = JsonUtil.convertJsonToData(jsonData);
//			if(bean != null){
//				DataPOJO dataPOJO = (DataPOJO)bean;
//				BasePOJO retPOJO = new BasePOJO();
//				retPOJO.setAction(dataPOJO.getAction());
//				retPOJO.setCode(dataPOJO.getCode());
//				retPOJO.setMessage(dataPOJO.getMessage());
//				bean = retPOJO;
//			}
//		}
//		if(bean == null){
//			return jsonData;
//		}
		return bean ;
	}
}
