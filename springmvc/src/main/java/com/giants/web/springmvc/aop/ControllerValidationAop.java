/**
 * 
 */
package com.giants.web.springmvc.aop;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import com.giants.common.exception.DataValidationException;

/**
 * @author vencent.lu
 *
 */
public class ControllerValidationAop {
	
	private Validator validator;
	private String errorMessageKey;
	private List<String> dontThrowExceptionsReturnTypes = new ArrayList<String>();
	
	public void validate(JoinPoint controller) throws Throwable {
		Object[] args = controller.getArgs();
		Object model = args[0];
		BindingResult bindingResult = (BindingResult)args[1];
		this.validator.validate(model, bindingResult);
		
		if (!this.dontThrowExceptionsReturnTypes.contains(controller
				.getSignature().toLongString().split(" ")[1])) {
			if (bindingResult.hasFieldErrors()){
				DataValidationException dataValidationException = new DataValidationException(
						this.errorMessageKey);
				for (FieldError fieldError : bindingResult.getFieldErrors()) {
					if (ArrayUtils.isEmpty(fieldError.getArguments())) {
						dataValidationException
								.addFieldError(dataValidationException
										.createFieldError(
												fieldError.getField(),
												fieldError.getDefaultMessage(),
												null));
					} else {
						List<String> arguments = new ArrayList<String>();
						for (Object arg : fieldError.getArguments()) {
							arguments.add(((MessageSourceResolvable) arg)
									.getDefaultMessage());
						}
						dataValidationException
								.addFieldError(dataValidationException
										.createFieldError(
												fieldError.getField(),
												fieldError.getDefaultMessage(),
												arguments.toArray()));
					}
				}
				throw dataValidationException;
			}
		}
		
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setDontThrowExceptionsReturnTypes(
			List<String> dontThrowExceptionsReturnTypes) {
		this.dontThrowExceptionsReturnTypes = dontThrowExceptionsReturnTypes;
	}

	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}

}
