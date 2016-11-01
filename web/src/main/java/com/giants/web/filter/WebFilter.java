/**
 * 
 */
package com.giants.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vencent.lu
 *
 */
public class WebFilter extends AbstractFilter {
	
	private static final ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
	
	public static HttpServletRequest getRequest() {
		return threadLocal.get();
	}

	/* (non-Javadoc)
	 * @see com.giants.web.filter.AbstractFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		threadLocal.set(request);
		try {
			chain.doFilter(request, response);
		} finally {
			threadLocal.set(null);
		}
	}

}
