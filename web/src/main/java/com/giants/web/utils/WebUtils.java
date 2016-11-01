/**
 * 
 */
package com.giants.web.utils;

import javax.servlet.http.HttpServletRequest;

import com.giants.web.filter.WebFilter;

/**
 * @author vencent.lu
 *
 */
public class WebUtils {
		
	public static Object getSessionAttribute(String name){
		return WebFilter.getRequest().getSession().getAttribute(name);
	}
	
	public static void setSessionAttribute(String name,Object value){
		WebFilter.getRequest().getSession().setAttribute(name, value);
	}
	
	public static String getHeader(String name){
		return WebFilter.getRequest().getHeader(name);
	}
	
	public static String getIpAddress(){
		HttpServletRequest request = WebFilter.getRequest();
		return getIpAddress(request);
	}
	
	/**
	 * 取得HttpRequest的简化函数.
	 */
	public static HttpServletRequest getRequest() {
	    return WebFilter.getRequest();
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");

			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

}
