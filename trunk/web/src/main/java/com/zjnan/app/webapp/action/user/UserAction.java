package com.zjnan.app.webapp.action.user;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.zjnan.app.exception.ServiceException;
import com.zjnan.app.model.Role;
import com.zjnan.app.model.User;
import com.zjnan.app.service.RoleManager;
import com.zjnan.app.service.User1Manager;
import com.zjnan.app.util.Page;
import com.zjnan.app.util.PropertyFilter;
import com.zjnan.app.webapp.action.CRUDActionSupport;
import com.zjnan.app.webapp.util.Struts2Utils;
import com.zjnan.app.webapp.util.WebUtils;

/**
 * 
 */
public class UserAction extends CRUDActionSupport<User> {

    @Autowired
    private User1Manager user1Manager;
    @Autowired
    private RoleManager roleManager;

    // 基本属性
    private User        entity;
    private Long        id;
    private Page<User>  page = new Page<User>(1); // 每页5条记录

    // 角色相关属性
    private List<Role>  allRoles;                // 全部可选角色列表
    private List<Long>  checkedRoleIds;          // 页面中钩选的角色id列表

    // 基本属性访问函数 //

    public User getModel() {
        return entity;
    }

    @Override
    protected void prepareModel() throws Exception {
        if (id != null) {
            entity = user1Manager.getUser(id.toString());
        } else {
            entity = new User();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Page<User> getPage() {
        return page;
    }

    // CRUD Action 函数 //

    @Override
    public String list() throws Exception {
        HttpServletRequest request = Struts2Utils.getRequest();
        List<PropertyFilter> filters = WebUtils
                .buildPropertyFilters(request);

        page = user1Manager.search(page, filters);
        return SUCCESS;
    }

    // @Override
    public String edit() throws Exception {
        allRoles = roleManager.getRoles(null);
        checkedRoleIds = entity.getRoleIds();
        return SUCCESS;
    }

    @Override
    public String save() throws Exception {
        // 根据页面上的checkbox 整合User的Roles Set
        WebUtils.mergeByCheckedIds(entity.getRoles(), checkedRoleIds,
                Role.class);
        System.out.println(entity.getRoleIds());
        user1Manager.save(entity);
        addActionMessage("保存用户成功");
        
        edit();
        return SUCCESS;
    }

    @Override
    public String delete() throws Exception {
        try {
            user1Manager.removeUser(id.toString());
            addActionMessage("删除用户成功");
        } catch (ServiceException e) {
            // logger.error(e.getMessage(), e);
            addActionMessage(e.getMessage());
        }
        return SUCCESS;
    }
    
    public String remove() throws Exception {
        try {
            user1Manager.removeUser(id.toString());
            Struts2Utils.renderText("删除用户成功");
        } catch (ServiceException e) {
            // logger.error(e.getMessage(), e);
            Struts2Utils.renderText(e.getMessage());
        }
        return null;
    }

    // 其他属性访问函数与Action函数 //

    public List<Role> getAllRoles() {
        return allRoles;
    }
    
    public void setAllRoles(List<Role> allRoles) {
        this.allRoles = allRoles;
    }

    public List<Long> getCheckedRoleIds() {
        return checkedRoleIds;
    }

    public void setCheckedRoleIds(List<Long> checkedRoleIds) {
        this.checkedRoleIds = checkedRoleIds;
    }

    public String checkLoginName() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String loginName = request.getParameter("loginName");
        String orgLoginName = request.getParameter("orgLoginName");

        if (this.isLoginNameUnique(loginName, orgLoginName)) {
            Struts2Utils.renderText("true");
        } else {
            Struts2Utils.renderText("false");
        }
        return null;
    }
    
    private boolean isLoginNameUnique(String name, String org) {
        return new Random().nextBoolean();
    }

    /**
     * @param userManager the userManager to set
     */
    public void setUser1Manager(User1Manager userManager) {
        this.user1Manager = userManager;
    }

    /**
     * @param roleManager the roleManager to set
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     *  this for validation on server side if you need
     * @return the entity
     */
    public User getUser() {
        return entity;
    }
}
