package com.zjnan.app.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;

import com.zjnan.app.dao.GenericDao;
import com.zjnan.app.util.Page;
import com.zjnan.app.util.PropertyFilter;
import com.zjnan.app.util.ReflectionUtils;
import com.zjnan.app.util.PropertyFilter.MatchType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="com.zjnan.app.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="com.zjnan.app.model.Foo"/&gt;
 *          &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoHibernate<T, PK extends Serializable> extends HibernateDaoSupport implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    protected Class<T> persistentClass;
	@Autowired
	public void setAsessionFactory(SessionFactory sessionFactory) {
		 setSessionFactory(sessionFactory);
	}
    /**
     * Constructor that takes in a class to see which type of entity to persist
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return super.getHibernateTemplate().loadAll(this.persistentClass);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        T entity = (T) super.getHibernateTemplate().get(this.persistentClass, id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        T entity = (T) super.getHibernateTemplate().get(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(T object) {
        return (T) super.getHibernateTemplate().merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        super.getHibernateTemplate().delete(this.get(id));
    }
    
   /** 
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   public List<T> findByNamedQuery(String queryName,
                                   Map<String, Object> queryParams) {
       
       String []params = new String[queryParams.size()];
       Object []values = new Object[queryParams.size()];
       int index = 0;
       Iterator<String> i = queryParams.keySet().iterator();
       while (i.hasNext()) {
           String key = i.next();
           params[index] = key;
           values[index++] = queryParams.get(key);
       }
       return getHibernateTemplate().findByNamedQueryAndNamedParam(
           queryName, 
           params, 
           values);
   }
   
   @SuppressWarnings("unchecked")
   public List<T> find(final String hql, final Object... values) {
       Assert.hasText(hql);
       return super.getHibernateTemplate().find(hql, values);
   }
   
   
//   ============================================================
   
   /**
    * @param persistentClass
    */
   public Page<T> findByCriteria(final Page<T> page, final Criterion... criterions) {
       Assert.notNull(page, "page不能为空");

       Criteria c = createCriteria(criterions);

       if (page.isAutoCount()) {
           int totalCount = countCriteriaResult(c);
           page.setTotalCount(totalCount);
       }

       setPageParameter(c, page);
       List result = c.list();
       page.setResult(result);
       return page;
   }
   
   public List<T> find(final Criterion... criterions) {
       return createCriteria(criterions).list();
   }
   
   public Criteria createCriteria(final Criterion... criterions) {
       Criteria criteria = getSession().createCriteria(persistentClass);
       for (Criterion c : criterions) {
           criteria.add(c);
       }
       return criteria;
   }
   
   /**
    * set pagination param
    * @param c
    * @param page
    * @return
    */
   protected Criteria setPageParameter(final Criteria c, final Page<T> page) {
       c.setFirstResult(page.getFirst());
       c.setMaxResults(page.getPageSize());

       if (page.isOrderBySetted()) {
           String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
           String[] orderArray = StringUtils.split(page.getOrder(), ',');

           Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

           for (int i = 0; i < orderByArray.length; i++) {
               if (Page.ASC.equals(orderArray[i])) {
                   c.addOrder(Order.asc(orderByArray[i]));
               } else {
                   c.addOrder(Order.desc(orderByArray[i]));
               }
           }
       }
       return c;
   }
   
   /**
    * Gets count
    * @param c
    * @return
    */
   @SuppressWarnings("unchecked")
   protected int countCriteriaResult(final Criteria c) {
       CriteriaImpl impl = (CriteriaImpl) c;

       // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
       Projection projection = impl.getProjection();
       ResultTransformer transformer = impl.getResultTransformer();

       List<CriteriaImpl.OrderEntry> orderEntries = null;
       try {
           orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
           ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
       } catch (Exception e) {
//           logger.error("不可能抛出的异常:{}", e.getMessage());
       }

       // 执行Count查询
       int totalCount = (Integer) c.setProjection(Projections.rowCount()).uniqueResult();

       // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
       c.setProjection(projection);

       if (projection == null) {
           c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
       }
       if (transformer != null) {
           c.setResultTransformer(transformer);
       }
       try {
           ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
       } catch (Exception e) {
//           logger.error("不可能抛出的异常:{}", e.getMessage());
       }

       return totalCount;
   }
   
   
   
   // 属性条件查询函数 //

   /**
    * 按属性查找对象列表,支持多种匹配方式.
    * 
    * @param matchType 目前支持的取值为"EQUAL"与"LIKE".
    */
   public List<T> findBy(final String propertyName, final Object value, final String matchTypeStr) {
       MatchType matchType = Enum.valueOf(MatchType.class, matchTypeStr);
       Criterion criterion = buildPropertyCriterion(propertyName, value, matchType);
       return find(criterion);
   }

   /**
    * 按属性过滤条件列表查找对象列表.
    */
   public List<T> find(final List<PropertyFilter> filters) {
       Criterion[] criterions = buildFilterCriterions(filters);
       return find(criterions);
   }

   /**
    * 按属性过滤条件列表分页查找对象.
    */
   public Page<T> find(final Page<T> page, final List<PropertyFilter> filters) {
       Criterion[] criterions = buildFilterCriterions(filters);
       return findByCriteria(page, criterions);
   }

   /**
    * 按属性条件列表创建Criterion数组,辅助函数.
    */
   protected Criterion[] buildFilterCriterions(final List<PropertyFilter> filters) {
       List<Criterion> criterionList = new ArrayList<Criterion>();
       for (PropertyFilter filter : filters) {
           String propertyName = filter.getPropertyName();

           boolean multiProperty = StringUtils.contains(propertyName, PropertyFilter.OR_SEPARATOR);
           if (!multiProperty) { //properNameName中只有一个属性的情况.
               Criterion criterion = buildPropertyCriterion(propertyName, filter.getValue(), filter.getMatchType());
               criterionList.add(criterion);
           } else {//properName中包含多个属性的情况,进行or处理.
               Disjunction disjunction = Restrictions.disjunction();
               String[] params = StringUtils.split(propertyName, PropertyFilter.OR_SEPARATOR);

               for (String param : params) {
                   Criterion criterion = buildPropertyCriterion(param, filter.getValue(), filter.getMatchType());
                   disjunction.add(criterion);
               }
               criterionList.add(disjunction);
           }
       }
       return criterionList.toArray(new Criterion[criterionList.size()]);
   }

   /**
    * 按属性条件参数创建Criterion,辅助函数.
    */
   protected Criterion buildPropertyCriterion(final String propertyName, final Object value, final MatchType matchType) {
       Assert.hasText(propertyName, "propertyName不能为空");
       Criterion criterion = null;

       if (MatchType.EQ.equals(matchType)) {
           criterion = Restrictions.eq(propertyName, value);
       }
       if (MatchType.LIKE.equals(matchType)) {
           criterion = Restrictions.like(propertyName, (String) value, MatchMode.ANYWHERE);
       }

       return criterion;
   }
}
