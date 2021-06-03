/**
 * 
 */
package com.giants.web.springmvc.json;

import java.util.Map;

import com.giants.common.GiantsConstants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**
 * @author vencent.lu
 *
 */
@ApiModel(description="Response Json 统一格式")
public class JsonResult {

	@ApiModelProperty(value = "错误编码，前台根据不同的错误编码显示相应的错误提示。",
			allowableValues = "0:操作成功,1:没有登录，或没有进行认证,2:数据验证失败,3:业务操作异常,4:没有操作权限,5:数据操作异常,6:显示层异常,7:授权码非法,127:系统错误",
			required = true, example = "0")
	private byte code = GiantsConstants.ERROR_CODE_SUCCESS;

	@ApiModelProperty(value = "业务异常KEY,出现业务异常时返回，操作成功不返回些属性,前端可以基于KEY做分支处理。", example = "notFindxxxx")
	private String businessErrorKey;

	@ApiModelProperty(value = "提示信息,如果出现异常提示具体错误信息。", example = "操作成功!", required = true)
	private String message;
	
	/**
	 * 字段验证错误信息
	 */
	@ApiModelProperty(value = "数据合法性验证错误信息，出现验证错误才返回此属性",
					example = "{\"attribute1\": \"attribute1 不能为空.\", \"attribute2\": \"attribute2 不能为空.\"}")
	private Map<String, String> errMsg;

	@ApiModelProperty(value = "Response Body 返回值，当有返回值时返回此对象")
	private Object data;
	
	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, String> getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(Map<String, String> errMsg) {
		this.errMsg = errMsg;
	}

	public String getBusinessErrorKey() {
		return businessErrorKey;
	}

	public void setBusinessErrorKey(String businessErrorKey) {
		this.businessErrorKey = businessErrorKey;
	}	

}
