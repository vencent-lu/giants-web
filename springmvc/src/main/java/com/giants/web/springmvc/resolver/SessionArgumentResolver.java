/**
 * 
 */
package com.giants.web.springmvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author vencent.lu
 * 
 * spring mvc 3.X
 *
 */
public class SessionArgumentResolver implements WebArgumentResolver {
	
	private SessionAttribute[] sessionAttributes;

	/* (non-Javadoc)
	 * @see org.springframework.web.bind.support.WebArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.context.request.NativeWebRequest)
	 */
	@Override
	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		if (this.sessionAttributes != null) {
			for (SessionAttribute attribute : this.sessionAttributes) {
				if (methodParameter.getParameterType().equals(
						attribute.getType())
						&& methodParameter.getParameterName().equals(
								attribute.getName())) {
					return webRequest.getAttribute(attribute.getName(), RequestAttributes.SCOPE_SESSION); 
				}
			}
		}
		return UNRESOLVED;
	}

	public void setSessionAttributes(SessionAttribute[] sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}
	
	public void setSessionAttribute(SessionAttribute sessionAttribute) {
		this.sessionAttributes = new SessionAttribute[] { sessionAttribute };
	}

}
