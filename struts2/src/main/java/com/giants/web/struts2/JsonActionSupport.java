/**
 * 
 */
package com.giants.web.struts2;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.giants.common.GiantsConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.FieldValidator;
import com.opensymphony.xwork2.validator.Validator;

/**
 * 处理JSON请求基类
 * @author vencent.lu 
 */
public abstract class JsonActionSupport extends ActionSupport {

	private static final long serialVersionUID = -6260614420003968743L;
	
	public static final String VALIDATOR = "validator";
	
	private boolean success = true;	
	/**
     * 错误编码，前台根据不同的错误编码显示相应的错误提示
     * 0：操作成功
     * 1：数据验证失败
     * 2：没有登录，或没有进行认证
     * 3：没有操作权限
     * 4：业务操作异常
     * 5: 数据操作异常
     * 127：系统错误
     */
	private byte errorCode = GiantsConstants.ERROR_CODE_SUCCESS;
	private String message;	
	private ActionValidatorManager actionValidatorManager;
	private List<GiantsValidator> validators;
	private Map<String,List<String>> fieldValidationError;	
	
	@SkipValidation
	public String validator() throws Exception {
		List<Validator> validatorList = actionValidatorManager.getValidators(this.getClass(),null);
		if (!validatorList.isEmpty()) {
			this.validators = new ArrayList<GiantsValidator>();
			for (Validator<?> v : validatorList) {
				FieldValidator fv = (FieldValidator) v;
				GiantsValidator val = new GiantsValidator(fv.getFieldName(),
						fv.getMessage(fv), fv.getValidatorType());
				Class<?> valClass = fv.getClass();
				for (Field field : valClass.getDeclaredFields()) {
					String fieldName = field.getName();
					StringBuffer sb = new StringBuffer("get");
					sb.append(StringUtils.capitalize(fieldName));
					try {
						Method paramGetMethod = valClass.getDeclaredMethod(sb.toString());
						val.putParam(fieldName, paramGetMethod.invoke(fv));
					} catch (NoSuchMethodException e) { }					
				}
				this.validators.add(val);
			}
		} else {
			this.setSuccess(false);
			this.setMessage(this.getText("not.find.validator"));
		}
		return JsonActionSupport.VALIDATOR;
	}
	
	@Inject
	public void setActionValidatorManager(
			ActionValidatorManager actionValidatorManager) {
		this.actionValidatorManager = actionValidatorManager;		
	}

	public List<GiantsValidator> getValidators() {
		return validators;
	}
	
	public Map<String,List<String>> getFieldValidationError(){		
		return fieldValidationError;
	}
	
	public void setFieldValidationError(
			Map<String, List<String>> fieldValidationError) {
		this.fieldValidationError = fieldValidationError;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(byte errorCode) {
		this.errorCode = errorCode;
	}

}
