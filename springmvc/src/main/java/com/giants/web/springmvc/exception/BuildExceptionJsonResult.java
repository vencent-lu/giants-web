/**
 * 
 */
package com.giants.web.springmvc.exception;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.giants.common.GiantsConstants;
import com.giants.common.exception.BusinessException;
import com.giants.common.exception.DataValidationException;
import com.giants.common.exception.GiantsException;
import com.giants.common.exception.DataValidationException.FieldError;
import com.giants.web.springmvc.json.JsonResult;

/**
 * @author vencent.lu
 *
 */
public class BuildExceptionJsonResult {
	
	private final static Logger logger = LoggerFactory.getLogger(BuildExceptionJsonResult.class);
	
	public static JsonResult build(Throwable e, ResourceBundleMessageSource resource, Locale locale) {
		if(e instanceof UndeclaredThrowableException){
			UndeclaredThrowableException ute = (UndeclaredThrowableException)e;
			return build(ute.getUndeclaredThrowable(), resource, locale);
		}else if(e instanceof GiantsException){
			JsonResult result = new JsonResult();
			GiantsException ge = (GiantsException)e;
			result.setCode(ge.buildErrorCode());
			if (StringUtils.isNotEmpty(ge.getErrorMessage())) {
				result.setMessage(ge.getErrorMessage());
			} else {
				result.setMessage(getMessage(resource, ge.getErrorMessageKey(), locale, 
						ge	.getMessageArgs() == null ? null : ge.getMessageArgs()));
			}
			result.setData(ge.getErrorData());
			if (e instanceof DataValidationException) {
				DataValidationException dve = (DataValidationException) e;
				if (CollectionUtils.isNotEmpty(dve.getFieldErrors())) {
					List<FieldError> fieldErrors = dve.getFieldErrors();
					Map<String,String> fieldErrorMap  = new HashMap<String,String>();
					for (FieldError fieldError : fieldErrors){
						if (ArrayUtils.isEmpty(fieldError.getArgs())) {
							fieldErrorMap.put(fieldError.getFieldName(),
									getMessage(resource, fieldError.getErrorMsgKey(), locale));
						} else {
							List<Object> args = new ArrayList<Object>();
							for (Object arg : fieldError.getArgs()) {
								args.add(getMessage(resource, arg.toString(), locale));
							}
							fieldErrorMap.put(
									fieldError.getFieldName(),
									getMessage(resource, fieldError.getErrorMsgKey(), locale, args.toArray()));
						}						
					}
					result.setErrMsg(fieldErrorMap);
				}				
			} else if (e instanceof BusinessException) {
				result.setBusinessErrorKey(((BusinessException)e).getErrorKey());
			}
			return result;
		}else{
			logger.error(MarkerFactory.getMarker("NOTIFY_DEV"), e.getMessage(), e);
			JsonResult result = new JsonResult();
			result.setCode(GiantsConstants.ERROR_CODE_SYSTEM_ERROR);
			result.setMessage(getMessage(resource, "operation.systemerror", locale));
			return result;
		}
	}
	
	private static String getMessage(ResourceBundleMessageSource resource,
			String resourceKey, Locale locale, Object... args) {
		try {
			return resource.getMessage(resourceKey, args, locale);
		} catch (NoSuchMessageException e) {
			return resourceKey;
		}
	}

}
