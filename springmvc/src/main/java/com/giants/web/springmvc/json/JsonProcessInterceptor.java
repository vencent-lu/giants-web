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
	 * @param jsonBytes json Bytes
	 */
	void readBefore(byte[] jsonBytes);
	
	/**
	 * 读入JSON后,执行操作
	 * @param object object
	 */
	void readAfter(Object object);
	
	/**
	 * 写入JSON前,执行操作
	 * @param object object
	 */
	void writeBefore(Object object);
	/**
	 * 写入JSON后,执行操作
	 * @param jsonStr json String
	 */
	void writeAfter(String jsonStr);

}
