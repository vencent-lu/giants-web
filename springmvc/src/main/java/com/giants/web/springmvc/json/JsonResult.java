/**
 * 
 */
package com.giants.web.springmvc.json;

import java.util.Map;

import com.giants.common.GiantsConstants;

/**
 * @author vencent.lu
 *
 */
public class JsonResult {	
	/**
     * 错误编码，前台根据不同的错误编码显示相应的错误提示
     * 0：操作成功
     * 1：没有登录，或没有进行认证
     * 2：数据验证失败
     * 3：业务操作异常
     * 4：没有操作权限
     * 5：数据操作异常
     * 127：系统错误
     */
	private byte code = GiantsConstants.ERROR_CODE_SUCCESS;
	
	private String businessErrorKey;

	private String message;	
	
	/**
	 * 字段验证错误信息
	 */
	private Map<String, String> ErrMsg;
	
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
		return ErrMsg;
	}

	public void setErrMsg(Map<String, String> errMsg) {
		ErrMsg = errMsg;
	}

	public String getBusinessErrorKey() {
		return businessErrorKey;
	}

	public void setBusinessErrorKey(String businessErrorKey) {
		this.businessErrorKey = businessErrorKey;
	}	

}
