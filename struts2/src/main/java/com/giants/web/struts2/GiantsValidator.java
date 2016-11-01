/**
 * 
 */
package com.giants.web.struts2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
public class GiantsValidator implements Serializable {
	
	private static final long serialVersionUID = -8683043910377966934L;
	
	private String fieldName;
	private String message;
	private Map<String,Object> param;
	private String validatorType;

	public GiantsValidator(String fieldName, String message, String validatorType) {
		super();
		this.fieldName = fieldName;
		this.message = message;
		this.validatorType = validatorType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public String getValidatorType() {
		return validatorType;
	}

	public void setValidatorType(String validatorType) {
		this.validatorType = validatorType;
	}
	
	public void putParam(String paramName,Object value) {
		if (this.param == null) {
			this.param = new HashMap<String,Object>();
		}
		this.param.put(paramName, value);
	}

}
