/**
 * 
 */
package com.giants.web.springmvc.resolver;

/**
 * @author vencent.lu
 *
 */
public class SessionAttribute {
	
	private String name;
	private Class<?> type;	
		
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

}
