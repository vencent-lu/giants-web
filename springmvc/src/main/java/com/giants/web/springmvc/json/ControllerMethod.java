/**
 * 
 */
package com.giants.web.springmvc.json;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public class ControllerMethod implements Serializable {

	private static final long serialVersionUID = 5958388001692232897L;
	
	private String methodName;
	private String returnType;
	
	private boolean returnValue;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isReturnValue() {
		return returnValue;
	}

	public void setReturnValue(boolean returnValue) {
		this.returnValue = returnValue;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
		this.returnValue = !returnType.equals("void");
	}
	
}
