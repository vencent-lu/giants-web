/**
 * 
 */
package com.giants.web.springmvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author vencent.lu
 * 
 * spring mvc 4.X
 *
 */
public class SessionHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {
	
	private SessionAttribute sessionAttribute;

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (this.sessionAttribute != null) {
			if (parameter.getParameterType().equals(
					this.sessionAttribute.getType())
					&& parameter.getParameterName().equals(
							this.sessionAttribute.getName())) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		return webRequest.getAttribute(this.sessionAttribute.getName(), RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * @return the sessionAttribute
	 */
	public SessionAttribute getSessionAttribute() {
		return sessionAttribute;
	}

	/**
	 * @param sessionAttribute the sessionAttribute to set
	 */
	public void setSessionAttribute(SessionAttribute sessionAttribute) {
		this.sessionAttribute = sessionAttribute;
	}
	
	

}
