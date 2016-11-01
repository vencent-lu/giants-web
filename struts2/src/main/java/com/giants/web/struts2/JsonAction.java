package com.giants.web.struts2;

import com.giants.common.GiantsConstants;
import com.giants.web.struts2.exception.BusinessExceptionHandler;

/**
 * @author vencent.lu
 *
 */
public abstract class JsonAction extends JsonActionSupport {

	private static final long serialVersionUID = 7686697430183234098L;	
	
	public abstract void executeAction() throws Exception;

	@Override
	public String execute() throws Exception {
		try{
			this.executeAction();
			this.setSuccess(true);
			this.setErrorCode(GiantsConstants.ERROR_CODE_SUCCESS);
		}catch(Exception e){
			BusinessExceptionHandler.handleExceptionToJson(this, e);
			return JsonAction.ERROR;
		}
		return JsonAction.SUCCESS;
	}
	

}
