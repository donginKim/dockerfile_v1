package sample.dao.mysql;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.apache.commons.lang.StringUtils;
import sample.dao.AbstractDAO;
import sample.dao.DAOSql;
import sample.models.Group;
import sample.models.Organization;
import sample.models.State;

@Repository
public class MysqlDAO extends AbstractDAO{
	
	@Autowired
    @Qualifier("jdbcMysql") 
	private JdbcTemplate jdbcTemplate;
	
	public List<State> findAll() {
		return this.jdbcTemplate.query("select * from current_states", new RowMapper<State>() {
				public State mapRow(ResultSet rs, int rowNum) throws SQLException {
					State s = new State();
					s.setId(rs.getLong("id"));
					s.setStateCode(rs.getString("state_code"));
					s.setName(rs.getString("name"));
					return s;
				}
			});
	}

    public Map selectOrganizationMaxId(Organization param){
        return this.jdbcTemplate.queryForObject(DAOSql.SQL_SELECT_ORG_MAX_ID, new RowMapper<Map>() {
            public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map s = new HashMap();
                s.put("id", rs.getLong("id"));
                return s;
            }
        });
    }

    @SuppressWarnings("rawtypes")
	public List selectOrganizations(Organization param){
        return this.jdbcTemplate.query(DAOSql.SQL_SELECT_ORGS, new RowMapper<Map>() {
            @SuppressWarnings("unchecked")
			public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map s = new HashMap();
                s.put("id", rs.getLong("id"));
                s.put("label", rs.getString("label"));
                s.put("desc", StringUtils.replace(rs.getString("desc"), "null", "") );
                s.put("url", rs.getString("url"));
                s.put("created", rs.getString("created"));
                s.put("modified", rs.getString("modified"));
                return s;
            }
        });
    }

    public Map selectOrganizationId(Organization param){

        Object[] args = new Object[1];
        args[0] = param.getLabel();
        
        return this.jdbcTemplate.queryForObject(DAOSql.SQL_SELECT_ORGID_BY_LABEL, args, new RowMapper<Map>() {
            public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map s = new HashMap();
                s.put("id", rs.getLong("id"));
                return s;
            }
        });
    }
    
    public Map selectOrganization(Organization param){

        Object[] args = new Object[1];
        args[0] = param.getId();
        
        return this.jdbcTemplate.queryForObject(DAOSql.SQL_SELECT_ORG, args, new RowMapper<Map>() {
            public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map s = new HashMap();
                s.put("id", rs.getLong("id"));
                s.put("label", rs.getString("label"));
                s.put("desc", rs.getString("desc"));
                s.put("url", rs.getString("url"));
                s.put("created", rs.getString("created"));
                s.put("modified", rs.getString("modified"));
                return s;
            }
        });
    }
    
     public int insertOrganization(Organization param){
         Object[] args = new Object[4];
         args[0] = this.selectOrganizationMaxId(param).get("id");
         args[1] = param.getLabel();
         args[2] = param.getDesc();
         args[3] = param.getUrl();
         
          int count = this.jdbcTemplate.update(DAOSql.SQL_INSERT_ORG, args);
          
          return count;
        }
     
     public long selectMaxOrganizationId(Organization param){
    	 return (long)this.selectOrganizationMaxId(param).get("id");
     }
     
     public int updateOrganization(Organization param){
         Object[] args = new Object[4];
         args[0] = param.getLabel();
         args[1] = param.getDesc();
         args[2] = param.getUrl();
         args[3] = param.getId();
         
          int count = this.jdbcTemplate.update(DAOSql.SQL_UPDATE_ORG, args);
          
          return count;
     }
     
     public int deleteOrganization(Organization param){
         Object[] args = new Object[1];
         args[0] = param.getId();
         
          int count = this.jdbcTemplate.update(DAOSql.SQL_DELETE_ORG, args);
          
          return count;
     }

     public Map selectGroupMaxId(Group param){
         return this.jdbcTemplate.queryForObject(DAOSql.SQL_SELECT_GROUP_MAX_ID, new RowMapper<Map>() {
             public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                 Map s = new HashMap();
                 s.put("id", rs.getLong("id"));
                 return s;
             }
         });
     }
     
     public List selectGroups(Group param){
         
         Object[] args = new Object[1];
         args[0] = param.getOrgId();
         
         return this.jdbcTemplate.query(DAOSql.SQL_SELECT_GROUPS, args, new RowMapper<Map>() {
             @SuppressWarnings("unchecked")
			public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                 Map s = new HashMap();
                 s.put("id", rs.getLong("id"));
                 s.put("orgId", rs.getLong("org_id"));
                 s.put("parent_id", rs.getLong("parent_id"));
                 s.put("label", rs.getString("label"));
                 
                 if(rs.getString("desc") == null)
                	 s.put("desc", "");
                 else
                	 s.put("desc", rs.getString("desc"));
                 
                 s.put("thumb_img_name", rs.getString("thumb_img_name"));
                 s.put("thumb_img_path", rs.getString("thumb_img_path"));
                 s.put("url", rs.getString("url"));
                 s.put("created", rs.getString("created"));
                 s.put("modified", rs.getString("modified"));
                 
                 return s;
             }
         });
     }

     public Map selectGroup(Group param){
         
         Object[] args = new Object[1];
         args[0] = param.getId();
         
          Map map = this.jdbcTemplate.queryForObject(DAOSql.SQL_SELECT_GROUP, args, new RowMapper<Map>() {
             public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                 Map s = new HashMap();
                 s.put("id", rs.getLong("id"));
                 s.put("orgId", rs.getLong("org_id"));
                 s.put("parent_id", rs.getLong("parent_id"));
                 s.put("label", rs.getString("label"));
                 s.put("desc", rs.getString("desc"));
                 s.put("thumb_img_name", rs.getString("thumb_img_name"));
                 s.put("thumb_img_path", rs.getString("thumb_img_path"));
                 s.put("url", rs.getString("url"));
                 s.put("created", rs.getString("created"));
                 s.put("modified", rs.getString("modified"));
                 return s;
             }
             
         });
          
          return map;
     }
     
      public int insertGroup(Group param){
          Object[] args = new Object[8];
          args[0] = this.selectGroupMaxId(param).get("id");
          args[1] = param.getOrgId();
          args[2] = ("".equals(param.getParent_id()) || param.getParent_id() == null) ? null : Integer.parseInt(param.getParent_id());
          args[3] = param.getLabel();
          args[4] = param.getDesc();
          args[5] = param.getThumb_img_name();
          args[6] = param.getThumb_img_path();
          args[7] = param.getUrl();
          
           int count = this.jdbcTemplate.update(DAOSql.SQL_INSERT_GROUP, args);
           
           return count;
         }
      
      public int updateGroup(Group param){
          Object[] args = new Object[8];
          args[0] = param.getOrgId();
          args[1] = ("".equals(param.getParent_id()) || param.getParent_id() == null) ? null : Integer.parseInt(param.getParent_id());
          args[2] = param.getLabel();
          args[3] = param.getDesc();
          args[4] = param.getThumb_img_name();
          args[5] = param.getThumb_img_path();
          args[6] = param.getUrl();
          args[7] = param.getId();
          
          
           int count = this.jdbcTemplate.update(DAOSql.SQL_UPDATE_GROUP, args);
           
           return count;
      }
      
      public int deleteGroup(Group param){
          Object[] args = new Object[1];
          args[0] = param.getId();
          
           int count = this.jdbcTemplate.update(DAOSql.SQL_DELETE_GROUP, args);
           
           return count;
      }
}
