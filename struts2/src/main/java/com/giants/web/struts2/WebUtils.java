/**
 * 
 */
package com.giants.web.struts2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author vencent.lu
 *
 */
public class WebUtils {	
	
	public static Map<String,Object> getSession(){
		return ActionContext.getContext().getSession();
	}
	
	public static Object getSessionAttribute(String name){
		return ActionContext.getContext().getSession().get(name);
	}
	
	public static void setSessionAttribute(String name,Object value){
		ActionContext.getContext().getSession().put(name, value);
	}
	
	/**
	 * 取得HttpRequest的简化函数.
     * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
	    if(ActionContext.getContext() != null) {
	        return ServletActionContext.getRequest();
	    }
	    return null;
	}
	
	/**
     * 获取客户端真实IP
     *
     * @return String
     */
    public static String getRemoteAddr() {
    	HttpServletRequest request = getRequest();
        String address =  request.getHeader("X-Real-IP");
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("X-Forwarded-For");
        } else {
            return address;
        }
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("Proxy-Client-IP");
        } else {
            return address;
        }
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("WL-Proxy-Client-IP");
        } else {
            return address;
        }
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("HTTP_CLIENT_IP");
        } else {
            return address;
        }
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("HTTP_X_FORWARDED_FOR");
        } else {
            return address;
        }
        
        if(StringUtils.isEmpty(address) || "unknown".equalsIgnoreCase(address)){
            address =  request.getHeader("Proxy-Client-IP");
        } else {
            return address;
        }

        return request.getRemoteAddr();
    }

}
