package sample.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Orgs")
public class Organization {

    @Id
	private String id;
	private String label;
    private String desc;
    private String url;
    private String created;
    private String modified;
        
    private String dbType;
    
    
    private List<Group> groups;

    public Organization() {
        
    }

    public Organization(Map map) {
        id = (map.get("id") != null) ? map.get("id").toString() : null;
        if(id == null)
            id = (map.get("orgId") != null) ? map.get("orgId").toString() : null;
        label = (map.get("label") != null) ? map.get("label").toString() : null;
        desc = (map.get("desc") != null) ? map.get("desc").toString() : null;
        url = (map.get("url") != null) ? map.get("url").toString() : null;
        created = (map.get("created") != null) ? map.get("created").toString() : null;
        modified = (map.get("modified") != null) ? map.get("modified").toString() : null;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public List<Group> getGroups() {
        if(groups == null)
            groups = new ArrayList<>();
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
	public String toString() {
		return "Organization [id=" + id 
		        + ", label=" + label 
		        + ", desc=" + desc 
		        + ", url=" + url 
		        + ", created=" + created 
		        + ", modified=" + modified + "]";
	}
    
    public Map toMap(){
        Map map = new HashMap();
        map.put("id", id );
        map.put("label", label); 
        map.put("desc", desc); 
        map.put("url", url); 
        map.put("created", created); 
        map.put("modified", modified);
        return map;
    }

}
