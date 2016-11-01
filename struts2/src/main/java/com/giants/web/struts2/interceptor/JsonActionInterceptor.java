/**
 * 
 */
package com.giants.web.struts2.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.giants.web.struts2.JsonActionSupport;
import com.giants.web.struts2.exception.BusinessExceptionHandler;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author vencent.lu
 *
 */
public class JsonActionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4133700849118936547L;
	
	protected static final Logger logger  = LoggerFactory.getLogger(JsonActionInterceptor.class);

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		if(action instanceof JsonActionSupport){
			JsonActionSupport jsonAction = (JsonActionSupport)action;
			try{
				return invocation.invoke();
			}catch(Exception e){
				return BusinessExceptionHandler.handleExceptionToJson(jsonAction, e);
			}
		}else{
			return invocation.invoke();
		}		
	}	

}
