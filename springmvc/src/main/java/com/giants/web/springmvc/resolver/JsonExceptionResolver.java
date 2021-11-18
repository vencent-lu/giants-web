package com.giants.web.springmvc.resolver;

import com.giants.common.exception.GiantsException;
import com.giants.common.regex.Pattern;
import com.giants.web.springmvc.json.JsonpResult;
import org.apache.commons.lang.ArrayUtils;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 将异常直接返回 最终序列化成 json 输出
 * date time: 2021/4/9 11:16
 * @author vencent.lu
 * @since 1.1.3
 */
public class JsonExceptionResolver implements HandlerExceptionResolver {

    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String[] jsonpQueryParamNames;

    private boolean includeModelAndView = false;
    private int responseExceptionStatus = 600;
    private List<HttpMessageConverter<Object>> messageConverters;
    private ResourceBundleMessageSource resourceBundleMessageSource;

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        if (!(ex instanceof GiantsException)) {
            return null;
        }
        boolean isReturnJson = false;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (this.includeModelAndView) {
            isReturnJson = true;
        } else if (handler != null
                && (AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RestController.class) != null
                || AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class) != null)) {
            isReturnJson = true;
        }

        if (isReturnJson) {
            if (this.messageConverters != null) {
                if (this.resourceBundleMessageSource == null) {
                    this.resourceBundleMessageSource = WebApplicationContextUtils
                            .getWebApplicationContext(request.getServletContext())
                            .getBean(ResourceBundleMessageSource.class);
                }
                Object result = ex;
                response.setStatus(this.responseExceptionStatus);
                HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
                if (ArrayUtils.isNotEmpty(this.jsonpQueryParamNames)) {
                    for (String name : this.jsonpQueryParamNames) {
                        String value = request.getParameter(name);
                        if (value != null) {
                            if (!CALLBACK_PARAM_PATTERN.matches(value)) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Ignoring invalid jsonp parameter value: " + value);
                                }
                                continue;
                            }
                            MediaType contentTypeToUse = new MediaType("application", "javascript");
                            outputMessage.getHeaders().setContentType(contentTypeToUse);
                            JsonpResult jsonpResult = new JsonpResult();
                            jsonpResult.setJsonpFunction(value);
                            jsonpResult.setValue(result);
                            result = jsonpResult;
                        }
                    }
                }
                HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
                List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
                if (acceptedMediaTypes.isEmpty()) {
                    acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
                }
                MediaType.sortByQualityValue(acceptedMediaTypes);
                Class<?> returnValueType = result.getClass();
                for (MediaType acceptedMediaType : acceptedMediaTypes) {
                    for (HttpMessageConverter<Object> messageConverter : this.messageConverters) {
                        if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                            try {
                                messageConverter.write(result, acceptedMediaType, outputMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("HttpMessageConverter Error!", e);
                            }
                            try {
                                outputMessage.getBody().flush();
                                return new ModelAndView();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
            if (logger.isWarnEnabled()) {
                logger.warn("Could not find HttpMessageConverter that supports return type Json");
            }
        }

        return null;
    }

    public boolean isIncludeModelAndView() {
        return includeModelAndView;
    }

    public void setIncludeModelAndView(boolean includeModelAndView) {
        this.includeModelAndView = includeModelAndView;
    }

    public void setMessageConverters(List<HttpMessageConverter<Object>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void setResponseExceptionStatus(int responseExceptionStatus) {
        this.responseExceptionStatus = responseExceptionStatus;
    }

    public void setJsonpQueryParamName(String jsonpQueryParamName) {
        this.jsonpQueryParamNames = new String[]{jsonpQueryParamName};
    }

    public void setJsonpQueryParamNames(String... jsonpQueryParamNames) {
        this.jsonpQueryParamNames = jsonpQueryParamNames;
    }
}
