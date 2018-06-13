/**
 * 
 */
package com.giants.web.springmvc.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.giants.common.regex.Pattern;
import com.giants.web.springmvc.json.FastJsonHttpMessageConverter;
import com.giants.web.springmvc.json.JsonpResult;

/**
 * @author vencent.lu
 * 支持jsonp格式返回数据(配置spring mvc config)
 * 不推荐使用这个类 jsonp 相关功能已经合并到 @JsonResultResponseAdvice
 */
@Deprecated
public class JsonpResultResponseAdvice implements ResponseBodyAdvice<Object> {
	
	private static final Logger   logger = LoggerFactory.getLogger(JsonpResultResponseAdvice.class);	
	private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
	
	private final String[] jsonpQueryParamNames;	

	public JsonpResultResponseAdvice(String... queryParamNames) {
		super();
		if (ArrayUtils.isEmpty(queryParamNames)) {
			throw new IllegalArgumentException("At least one query param name is required");
		}
		this.jsonpQueryParamNames = queryParamNames;
	}
	
	protected boolean isValidJsonpQueryParam(String value) {
		return CALLBACK_PARAM_PATTERN.matches(value);
	}
	
	protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
		return new MediaType("application", "javascript");
	}

	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		for (String name : this.jsonpQueryParamNames) {
			String value = servletRequest.getParameter(name);
			if (value != null) {
				if (!this.isValidJsonpQueryParam(value)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Ignoring invalid jsonp parameter value: " + value);
					}
					continue;
				}
				MediaType contentTypeToUse = getContentType(selectedContentType, request, response);
				response.getHeaders().setContentType(contentTypeToUse);
				JsonpResult jsonpResult = new JsonpResult();
				jsonpResult.setJsonpFunction(value);
				jsonpResult.setValue(body);
				return jsonpResult;
			}
		}
		return body;
	}

}
