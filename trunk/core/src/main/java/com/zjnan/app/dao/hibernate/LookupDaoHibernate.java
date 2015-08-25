package com.zjnan.app.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zjnan.app.dao.LookupDao;
import com.zjnan.app.model.Role;

/**
 * Hibernate implementation of LookupDao.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Repository("lookupDao")
public class LookupDaoHibernate extends UniversalDaoHibernate implements LookupDao {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        log.debug("Retrieving all role names...");

        return getHibernateTemplate().find("from Role order by name");
    }
}
