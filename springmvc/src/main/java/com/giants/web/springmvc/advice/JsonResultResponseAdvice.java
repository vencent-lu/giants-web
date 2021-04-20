/**
 * 
 */
package com.giants.web.springmvc.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.giants.common.GiantsConstants;
import com.giants.common.regex.Pattern;
import com.giants.web.springmvc.json.JsonResult;
import com.giants.web.springmvc.json.JsonpResult;

/**
 * @author vencent.lu
 *
 */
public class JsonResultResponseAdvice implements ResponseBodyAdvice<Object> {
	
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
    protected final Logger logger = LoggerFactory.getLogger(getClass());    
    private String[] jsonpQueryParamNames;
    
	private ResourceBundleMessageSource resourceBundleMessageSource;
		
	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse servletResponse = ((ServletServerHttpResponse)response).getServletResponse();
		if (this.resourceBundleMessageSource == null) {
			this.resourceBundleMessageSource = WebApplicationContextUtils
					.getWebApplicationContext(servletRequest.getServletContext())
					.getBean(ResourceBundleMessageSource.class);
		}
		JsonResult result = new JsonResult();
		result.setData(body);
		if (servletResponse.getStatus() == 200) {
			result.setCode(GiantsConstants.ERROR_CODE_SUCCESS);
			result.setMessage(this.resourceBundleMessageSource.getMessage("operation.success", null, servletRequest.getLocale()));
		} else {
			result.setCode(GiantsConstants.ERROR_CODE_SYSTEM_ERROR);
			result.setMessage(this.resourceBundleMessageSource.getMessage("operation.systemerror", null, servletRequest.getLocale()));
		}
		if (ArrayUtils.isNotEmpty(this.jsonpQueryParamNames)) {
		    for (String name : this.jsonpQueryParamNames) {
	            String value = servletRequest.getParameter(name);
	            if (value != null) {
	                if (!CALLBACK_PARAM_PATTERN.matches(value)) {
	                    if (logger.isDebugEnabled()) {
	                        logger.debug("Ignoring invalid jsonp parameter value: " + value);
	                    }
	                    continue;
	                }
	                MediaType contentTypeToUse = new MediaType("application", "javascript");
	                response.getHeaders().setContentType(contentTypeToUse);
	                JsonpResult jsonpResult = new JsonpResult();
	                jsonpResult.setJsonpFunction(value);
	                jsonpResult.setValue(result);
	                return jsonpResult;
	            }
	        }
		}
		return result;
	}
	
	public void setJsonpQueryParamName(String jsonpQueryParamName) {
        this.jsonpQueryParamNames = new String[]{jsonpQueryParamName};
    }

    public void setJsonpQueryParamNames(String... jsonpQueryParamNames) {
        this.jsonpQueryParamNames = jsonpQueryParamNames;
    }

}
