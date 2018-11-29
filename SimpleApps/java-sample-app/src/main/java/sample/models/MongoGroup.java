
package sample.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <pre>
 * sample.models 
 *    |_ GroupMongo.java
 * 
 * </pre>
 * 
 * @date : 2016. 2. 16. 오후 5:06:54
 * @version :
 * @author : csupshin
 */
@Document(collection = "Groups")
public class MongoGroup {

	@Id
	private String id;

	private String label;
	
	private String desc;
	
	private String url;
	
	private String created;
	
	private String modified;

	@Id
	private ObjectId orgId;

	private String parent_id;

	private String parentId;

	private String thumb_img_name;
	
	private String thumb_img_path;

	private String thumbImgName;
	
	private String thumbImgPath;

	private String dbType;

	public MongoGroup() {
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
	
	public ObjectId getOrgId() {
		return orgId;
	}

	public void setOrgId(ObjectId orgId) {
		this.orgId = orgId;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getThumb_img_name() {
		return thumb_img_name;
	}

	public void setThumb_img_name(String thumb_img_name) {
		this.thumb_img_name = thumb_img_name;
	}

	public String getThumb_img_path() {
		return thumb_img_path;
	}

	public void setThumb_img_path(String thumb_img_path) {
		this.thumb_img_path = thumb_img_path;
	}

	public String getThumbImgName() {
		return thumbImgName;
	}

	public void setThumbImgName(String thumbImgName) {
		this.thumbImgName = thumbImgName;
	}

	public String getThumbImgPath() {
		return thumbImgPath;
	}

	public void setThumbImgPath(String thumbImgPath) {
		this.thumbImgPath = thumbImgPath;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public MongoGroup(Map map) {
        id = (map.get("id") != null) ? map.get("id").toString() : null;
        if(id == null)
            id = (map.get("group_id") != null) ? map.get("group_id").toString() : null;
        orgId = (map.get("orgId") != null) ? new ObjectId(map.get("orgId").toString()) : null;
        parent_id = (map.get("parent_id") != null) ? map.get("parent_id").toString() : null;
        parentId = (map.get("parentId") != null) ? map.get("parentId").toString() : null;
        label = (map.get("label") != null) ? map.get("label").toString() : null;
        desc = (map.get("desc") != null) ? map.get("desc").toString() : null;
        thumb_img_name = (map.get("thumb_img_name") != null) ? map.get("thumb_img_name").toString() : null;
        thumb_img_path = (map.get("thumb_img_path") != null) ? map.get("thumb_img_path").toString() : null;
        thumbImgName = (map.get("thumbImgName") != null) ? map.get("thumbImgName").toString() : null;
        thumbImgPath = (map.get("thumbImgPath") != null) ? map.get("thumbImgPath").toString() : null;
        url = (map.get("url") != null) ? map.get("url").toString() : null;
        created = (map.get("created") != null) ? map.get("created").toString() : null;
        modified = (map.get("modified") != null) ? map.get("modified").toString() : null;
    }


    
    public MongoGroup(Group map) {
        id = (map.getId() != null) ? map.getId() : null;
        orgId = (map.getOrgId() != null) ? new ObjectId(map.getOrgId()) : null;
        parent_id = (map.getParent_id() != null) ? map.getParent_id() : null;
        parentId = (map.getParentId() != null) ? map.getParentId() : null;
        label = (map.getLabel() != null) ? map.getLabel() : null;
        desc = (map.getDesc() != null) ? map.getDesc() : null;
        thumb_img_name = (map.getThumb_img_name() != null) ? map.getThumb_img_name() : null;
        thumb_img_path = (map.getThumb_img_path() != null) ? map.getThumb_img_path() : null;
        thumbImgName = (map.getThumbImgName() != null) ? map.getThumbImgName() : null;
        thumbImgPath = (map.getThumbImgPath() != null) ? map.getThumbImgPath() : null;
        url = (map.getUrl() != null) ? map.getUrl(): null;
        created = (map.getCreated() != null) ? map.getCreated() : null;
        modified = (map.getModified() != null) ? map.getModified() : null;
    }
	
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id );
        map.put("orgId", orgId );
        map.put("parent_id", parent_id );
        map.put("parentId", parentId );
        map.put("label", label); 
        map.put("desc", desc);
        map.put("thumb_img_name", thumb_img_name);
        map.put("thumb_img_path", thumb_img_path);
        map.put("thumbImgName", thumbImgName);
        map.put("thumbImgPath", thumbImgPath);
        map.put("url", url); 
        map.put("created", created); 
        map.put("modified", modified);
        return map;
    }
    @Override
   	public String toString() {
   		return "MongoGroup [id=" + id 
   		        + ", orgId=" + orgId
   		        + ", parent_id=" + parent_id
   		        + ", parentId=" + parentId
   		        + ", label=" + label 
   		        + ", desc=" + desc 
   		        + ", thumb_img_name=" + thumb_img_name
   		        + ", thumb_img_path=" + thumb_img_path
   		        + ", thumbImgName=" + thumbImgName
   		        + ", thumbImgPath=" + thumbImgPath
   		        + ", url=" + url 
   		        + ", created=" + created 
   		        + ", modified=" + modified + "]";
   	}
}
