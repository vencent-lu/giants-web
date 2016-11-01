/**
 * 
 */
package com.giants.web.struts2.interceptor;

import org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor;

import com.giants.common.GiantsConstants;
import com.giants.web.struts2.JsonActionSupport;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author vencent.lu
 *
 */
public class JsonValidationInterceptor extends
		AnnotationValidationInterceptor {

	private static final long serialVersionUID = 6779841847665344253L;
	
	@Override
	protected void doBeforeInvocation(ActionInvocation invocation)
			throws Exception {
		super.doBeforeInvocation(invocation);
		Object action = invocation.getAction();
		if(action instanceof JsonActionSupport){
			JsonActionSupport jsonAction = (JsonActionSupport)action;
			if(jsonAction.hasFieldErrors()){
				jsonAction.setSuccess(false);
				jsonAction.setErrorCode(GiantsConstants.ERROR_CODE_DATA_CHECK_FAILURE);
				jsonAction.setMessage(jsonAction.getText("request.data.validation.fail"));
				jsonAction.setFieldValidationError(jsonAction.getFieldErrors());
			}
		}
	}

}
