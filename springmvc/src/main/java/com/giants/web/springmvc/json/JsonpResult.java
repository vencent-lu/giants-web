/**
 * 
 */
package com.giants.web.springmvc.json;

/**
 * @author vencent.lu
 *
 */
public class JsonpResult {
	
	/**
	 * 回调JS function 名称
	 */
	private String jsonpFunction;
	
	/**
	 * 返回值
	 */
	private Object value;

	public String getJsonpFunction() {
		return jsonpFunction;
	}

	public void setJsonpFunction(String jsonpFunction) {
		this.jsonpFunction = jsonpFunction;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	

}
