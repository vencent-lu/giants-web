/**
 * 
 */
package com.giants.web.springmvc.json;

/**
 * JSON处理后拦截器
 * @author vencent.lu
 *
 */
public interface JsonProcessInterceptor {
	
	/**
	 * 读入JSON前,执行操作
	 * @param jsonBytes
	 */
	void readBefore(byte[] jsonBytes);
	
	/**
	 * 读入JSON后,执行操作
	 * @param object
	 */
	void readAfter(Object object);
	
	/**
	 * 写入JSON前,执行操作
	 * @param object
	 */
	void writeBefore(Object object);
	/**
	 * 写入JSON后,执行操作
	 * @param jsonStr
	 */
	void writeAfter(String jsonStr);

}
