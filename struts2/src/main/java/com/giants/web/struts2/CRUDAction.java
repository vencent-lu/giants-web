/**
 * 
 */
package com.giants.web.struts2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.giants.common.exception.BusinessException;
import com.giants.common.tools.Page;
import com.giants.web.struts2.exception.BusinessExceptionHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author vencent.lu
 *
 */
public abstract class CRUDAction<T> extends ActionSupport implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5757968263202191118L;
	
	public static final String RELOAD = "reload";
	
	private Page<T> page = new Page<T>(1,5);
	private Map<String,Object> queryFilters = new HashMap<String,Object>();
	
	private Serializable id;
	
	protected abstract int listCount(Map<String,Object> queryFilters) throws Exception;
	protected abstract List<T> listResultSet(Map<String,Object> queryFilters) throws Exception;
	protected abstract void createEntity() throws Exception;
	protected abstract void modifyEntity() throws Exception;
	protected abstract void loadEntity(Serializable id) throws Exception;
	protected abstract void deleteEntity() throws Exception;
	

	@Override
	@SkipValidation
	public String execute() throws Exception {
		return this.list();
	}
	
	@SkipValidation
	public String list() throws Exception{
		this.page.setTotal(this.listCount(this.queryFilters));
		this.queryFilters.put("offset", this.page.getOffset());
		this.queryFilters.put("size", this.page.getPageSize());
		page.setResults(this.listResultSet(this.queryFilters));
		return CRUDAction.SUCCESS;
	}
	
	public String input() throws Exception{
		return CRUDAction.INPUT;
	}
	
	public String save() throws Exception{
		try{
			if(this.id==null){
				this.createEntity();
			}else{
				this.modifyEntity();
			}
		}catch(BusinessException e){
			BusinessExceptionHandler.handleException(this, e);
			return CRUDAction.INPUT;
		}
		return CRUDAction.RELOAD;
	}
	
	@SkipValidation
	public String delete() throws Exception{
		this.deleteEntity();
		return CRUDAction.RELOAD;
	}
	
	@Override
	public void prepare() throws Exception {		 
		System.out.println(ServletActionContext.getActionMapping().getExtension());
		System.out.println(ServletActionContext.getActionMapping().getMethod());
		System.out.println(ServletActionContext.getActionMapping().getNamespace());
	}
	
	public void prepareInput() throws Exception {
		this.prepareModel();
	}

	public void prepareSave() throws Exception {
		this.prepareModel();
	}
	
	public void prepareDelete() throws Exception {
		this.prepareModel();
	}
	
	public void prepareModel() throws Exception {
		if(this.id!=null){
			try{
				this.loadEntity(this.id);
			}catch(BusinessException e){
				BusinessExceptionHandler.handleException(this, e);
			}
		}
	}
		
	public void setPage(Page<T> page) {
		this.page = page;
	}
		
	public void setId(Serializable id) {
		this.id = id;
	}
	public void setQueryFilters(Map<String, Object> queryFilters) {
		this.queryFilters = queryFilters;
	}
	
}
