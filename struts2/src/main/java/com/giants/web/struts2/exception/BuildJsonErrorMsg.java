/**
 * 
 */
package com.giants.web.struts2.exception;

import java.lang.reflect.UndeclaredThrowableException;

import com.giants.common.GiantsConstants;
import com.giants.common.exception.GiantsException;
import com.giants.web.struts2.JsonActionSupport;

/**
 * @author vencent.lu
 *
 */
public class BuildJsonErrorMsg {
	
	public static void build(JsonActionSupport action,
			Exception e) {
		if(e instanceof UndeclaredThrowableException){
			UndeclaredThrowableException ute = (UndeclaredThrowableException)e;
			build(action,(Exception)ute.getUndeclaredThrowable());
		}else if(e instanceof GiantsException){
			GiantsException ge = (GiantsException)e;
			action.setErrorCode(ge.buildErrorCode());
			action.setMessage(action.getText(ge.getErrorMessageKey(), ge.getMessageArgs()));
		}else{
			e.printStackTrace();
			action.setErrorCode(GiantsConstants.ERROR_CODE_SYSTEM_ERROR);
			action.setMessage(e.toString());
		}
	}

}
