/**
 * 
 */
package com.giants.web.springmvc.advice;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.giants.common.GiantsConstants;
import com.giants.web.springmvc.json.JsonResult;
import com.giants.web.utils.WebUtils;

/**
 * @author vencent.lu
 *
 */
public class JsonResultResponseAdvice implements ResponseBodyAdvice<Object> {
	
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
		if (this.resourceBundleMessageSource == null) {
			this.resourceBundleMessageSource = WebApplicationContextUtils
					.getWebApplicationContext(WebUtils.getRequest().getServletContext())
					.getBean(ResourceBundleMessageSource.class);
		}
		JsonResult result = new JsonResult();
		result.setData(body);
		result.setCode(GiantsConstants.ERROR_CODE_SUCCESS);
		result.setMessage(this.resourceBundleMessageSource.getMessage("operation.success", null, WebUtils.getRequest().getLocale()));
		return result;
	}

}
