/**
 * 
 */
package com.giants.web.springmvc.v3;

import java.lang.reflect.Method;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author vencent.lu
 *
 */
public interface MethodHandlerInterceptor {
	
	public void preInvoke(Method handlerMethod, Object handler, ServletWebRequest webRequest) throws Exception;
	
	public void postInvoke(Method handlerMethod, Object handler, ServletWebRequest webRequest, Object handlerReturn) throws Exception;


}
