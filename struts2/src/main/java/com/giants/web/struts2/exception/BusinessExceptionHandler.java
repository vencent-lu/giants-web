/**
 * 
 */
package com.giants.web.struts2.exception;

import com.giants.common.exception.BusinessException;
import com.giants.web.struts2.JsonActionSupport;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author vencent.lu
 *
 */
public class BusinessExceptionHandler {
		
	public static void handleException(ActionSupport action,BusinessException e){
		action.addActionError(action.getText(e.getErrorMessageKey(),e.getMessageArgs()));
	}
	
	public static String handleExceptionToJson(JsonActionSupport action,Exception e){
		action.setSuccess(false);
		BuildJsonErrorMsg.build(action, e);
		return JsonActionSupport.ERROR;
	}

}
