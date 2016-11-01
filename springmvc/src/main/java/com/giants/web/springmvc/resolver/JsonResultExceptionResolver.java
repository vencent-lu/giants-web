/**
 * 
 */
package com.giants.web.springmvc.resolver;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.giants.web.springmvc.exception.BuildExceptionJsonResult;
import com.giants.web.springmvc.json.JsonResult;
import com.giants.web.utils.WebUtils;

/**
 * @author vencent.lu
 *
 */
public class JsonResultExceptionResolver implements HandlerExceptionResolver {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<HttpMessageConverter<?>> messageConverters;
	
	private ResourceBundleMessageSource resourceBundleMessageSource;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			if (this.resourceBundleMessageSource == null) {
				this.resourceBundleMessageSource = WebApplicationContextUtils
						.getWebApplicationContext(request.getServletContext())
						.getBean(ResourceBundleMessageSource.class);
			}
			JsonResult result = BuildExceptionJsonResult.build(ex,this.resourceBundleMessageSource,WebUtils.getRequest().getLocale());
			HttpInputMessage inputMessage = new ServletServerHttpRequest(request);  
	        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();  
	        if (acceptedMediaTypes.isEmpty()) {  
	            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);  
	        }  
	        MediaType.sortByQualityValue(acceptedMediaTypes);  
	        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);  
	        Class<?> returnValueType = result.getClass();
	        if (this.messageConverters != null) {  
	            for (MediaType acceptedMediaType : acceptedMediaTypes) {
	                for (HttpMessageConverter messageConverter : this.messageConverters) {  
	                    if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {  
	                        try {
								messageConverter.write(result, acceptedMediaType, outputMessage);
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("HttpMessageConverter Error!", e);
							}
							return new ModelAndView();
	                    }  
	                }  
	            }  
	        }  
	        if (logger.isWarnEnabled()) {  
	            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);  
	        }
		}
		
		return null;
	}

	/**
	 * @param messageConverters the messageConverters to set
	 */
	public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}
	
	

}
