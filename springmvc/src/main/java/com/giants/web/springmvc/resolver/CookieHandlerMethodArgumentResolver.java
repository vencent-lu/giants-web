package com.giants.web.springmvc.resolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 
 * @author vencent.lu
 * spring mvc 4.X
 *
 */
public class CookieHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {
	
	private String cookieName;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (StringUtils.isNotEmpty(this.cookieName)) {
			if (parameter.getParameterType().equals(String.class)
					&& parameter.getParameterName().equals(this.cookieName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Cookie[] cookies = ((HttpServletRequest)webRequest.getNativeRequest()).getCookies();
		if (ArrayUtils.isNotEmpty(cookies)) {
			for(Cookie cookie : cookies) {
				if (cookie.getName().equals(this.cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

}
