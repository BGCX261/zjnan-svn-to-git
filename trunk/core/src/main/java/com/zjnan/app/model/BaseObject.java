package com.zjnan.app.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;


/**
 * Base class for Model objects. Child objects should implement toString(),
 * equals() and hashCode().
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@MappedSuperclass
public abstract class BaseObject implements Serializable {   
    
    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Common implement equals method
     */
    public boolean equals( Object obj )
    {
        if( this==obj ) return true;
        
        if( !( obj instanceof BaseObject ) )
            return false;
        
        BaseObject target = (BaseObject)obj;
        
        if( this.getId()!=null )
        {
            return this.getId().equals( target.getId() );
        }
        
        if( target.getId()!=null )
        {
            return false;
        }
        
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Generate the hash code
     */
    public int hashCode()
    {
        if( this.getId()!= null  )
        {
            return this.getId().hashCode();
        }
            
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Common implement toString method
     */
    public String toString()
    {
        return ReflectionToStringBuilder.toString( this );
    }
}
