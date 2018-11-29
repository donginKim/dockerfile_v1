/*
 * Copyright www.crossent.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of www.crossent.com.,LTD. ("Confidential Information").
 */
package sample.models; 

import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * <pre>
 * sample.models 
 *    |_ MongoOrganization.java
 * 
 * </pre>
 * @date : 2016. 2. 16. 오후 5:41:42
 * @version : 
 * @author : csupshin
 */
public class MongoOrganization {

    @Id
	private String id;
    
	private String label;
	
    private String desc;
    
    private String url;
    
    private String created;
    
    private String modified;
        
    private String dbType;    
    
    private List<MongoGroup> groups;

    public MongoOrganization() {        
    }
    
    public MongoOrganization(Map map) {
        id = (map.get("id") != null) ? map.get("id").toString() : null;
        if(id == null)
            id = (map.get("orgId") != null) ? map.get("orgId").toString() : null;
        label = (map.get("label") != null) ? map.get("label").toString() : null;
        desc = (map.get("desc") != null) ? map.get("desc").toString() : null;
        url = (map.get("url") != null) ? map.get("url").toString() : null;
        created = (map.get("created") != null) ? map.get("created").toString() : null;
        modified = (map.get("modified") != null) ? map.get("modified").toString() : null;
    }
    
    
}
