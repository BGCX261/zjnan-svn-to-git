package com.zjnan.app.webapp.action;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public abstract class CRUDActionSupport<T> extends BaseAction implements ModelDriven<T>,Preparable {

    protected Long        id;
    
    protected abstract void prepareModel() throws Exception;
   
    public abstract String list() throws Exception;
    
    public abstract String save() throws Exception;
    
    public abstract String delete() throws Exception;
    
    @Override
    public String execute() throws Exception{
        return list();
    }
    
    public void prepare() throws Exception {}
    
    public void prepareSave() throws Exception{
        prepareModel();
    }
    
    public void prepareEdit() throws Exception{
        prepareModel();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}
