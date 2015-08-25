package com.zjnan.app.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.zjnan.app.util.ReflectionUtils;

@Entity
@Table(name = "RESOURCES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource extends BaseObject implements Serializable {

    private static final long serialVersionUID = -7921146761824528148L;
   
    public static final String QUERY_BY_URL_TYPE = "FROM Resource WHERE resourceType=? ORDER BY orderNum ASC";

	public static final String URL_TYPE = "url";
	public static final String MENU_TYPE = "menu";

	private String resourceType; 

	private String value; 

	private double orderNum; 

	private Set<Authority> authorities = new LinkedHashSet<Authority>(); 

	@Column(name="resource_type")
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name="order_num")
	public double getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(double orderNum) {
		this.orderNum = orderNum;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(joinColumns = { @JoinColumn(name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
	@Fetch(FetchMode.JOIN)
	@OrderBy("id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Transient
	public String getAuthNames() throws Exception {
		return ReflectionUtils.fetchElementPropertyToString(authorities, "name", ",");
	}
}
