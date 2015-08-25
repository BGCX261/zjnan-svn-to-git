package com.zjnan.app.dao.hibernate;

import org.springframework.stereotype.Repository;
import com.zjnan.app.model.Resource;

@Repository
public class ResourceDao extends GenericDaoHibernate<Resource, Long> {

    /**
     * @param persistentClass
     */
    public ResourceDao() {
        super(Resource.class);
        
    }
}
