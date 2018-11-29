package sample.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sample.biz.OrganizationService;
import sample.biz.RabbitMQService;
import sample.models.Group;
import sample.models.Organization;

/**
 * <pre>
 * &#64;desc      : 사용자 정보 관련 처리 Controller
 * </pre>
 *
 * @package : com.crossent.paasxpert.user.web
 * @file_name : UserController.java
 * @date : 2015. 10. 13. 오전 9:07:51
 * @version :
 * @author : ihocho
 */
@RestController
public class OrganizationController {

    @Resource(name = "organizationService")
    private OrganizationService orgService;

    @Autowired(required=false)
	@Qualifier("rabbitMQService")
	RabbitMQService rabbitMQService;
    
    /**
     * <pre>
     * desc : 조직 및 그룹(하위조직) 리스트
     * </pre>
     * 
     * @Method Name : getOrgGroups
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/org-chart/{org_id}/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map getOrgGroups(@PathVariable("org_id") final String org_id, @PathVariable("db_type") final String db_type)
            throws Exception {
        
        Organization org = new Organization();
        org.setDbType(db_type);
        org.setId(org_id);

        return orgService.selectOrgGroups(org);
    }

    /**
     * <pre>
     * desc : 조직 리스트
     * </pre>
     * 
     * @Method Name : getOrganizations
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param db_type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orgs/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map getOrganizations(@PathVariable("db_type") final String db_type) throws Exception {
        
        Organization org = new Organization();
        org.setDbType(db_type);

        return orgService.selectOrganizations(org);
    }

    /**
     * <pre>
     * desc : 조직 정보
     * </pre>
     * 
     * @Method Name : getOrganization
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orgs/{org_id}/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map getOrganization(@PathVariable("org_id") final String org_id,
            @PathVariable("db_type") final String db_type) throws Exception {
                
        Organization org = new Organization();
        org.setDbType(db_type);
        org.setId(org_id);

        return orgService.selectOrganization(org);
    }

    /**
     * <pre>
     * desc : 조직 등록
     * </pre>
     * 
     * @Method Name : postOrganization
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param db_type
     * @param map
     * @return
     */
    @RequestMapping(value = "/orgs/{db_type}", method = RequestMethod.POST)
    @ResponseBody
    public Map postOrganization(@PathVariable("db_type") final String db_type, @RequestBody Organization org) {
        
        org.setDbType(db_type);

        Map result = orgService.insertOrganization(org);
        
        if(rabbitMQService != null)
			rabbitMQService.createQueue(db_type, org.getId());
        
        return result;
        
       
    }

    /**
     * <pre>
     * desc : 조직 수정
     * </pre>
     * 
     * @Method Name : putOraganization
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @param body
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/{db_type}", method = RequestMethod.PUT)
    public Map<String, String> putOraganization(@PathVariable("org_id") final String org_id,
            @PathVariable("db_type") final String db_type, @RequestBody Organization model ) {
        
        model.setDbType(db_type);
        model.setId(org_id);

        return orgService.updateOrganization(model);
    }

    /**
     * <pre>
     * desc : 조직 삭제
     * </pre>
     * 
     * @Method Name : deleteOrganization
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/{db_type}", method = RequestMethod.DELETE)
    public Map deleteOrganization(@PathVariable("org_id") final String org_id,
            @PathVariable("db_type") final String db_type) {
        
        Organization model = new Organization();
        model.setDbType(db_type);
        model.setId(org_id);

        return orgService.deleteOrganization(model);
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 리스트
     * </pre>
     * 
     * @Method Name : getGroups
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map getGroups(@PathVariable("org_id") final String org_id, @PathVariable("db_type") final String db_type) {
        
        Group model = new Group();
        model.setDbType(db_type);
        model.setOrgId(org_id);

        return orgService.selectGroups(model);
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 정보
     * </pre>
     * 
     * @Method Name : getGroup
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param group_id
     * @param db_type
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{group_id}/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map getGroup(@PathVariable("org_id") final String org_id, @PathVariable("group_id") final String group_id,
            @PathVariable("db_type") final String db_type) {
        
        Group model = new Group();
        model.setDbType(db_type);
        model.setOrgId(org_id);
        model.setId(group_id);

        return orgService.selectGroup(model);
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 등록
     * </pre>
     * 
     * @Method Name : postGroup
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @param map
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{db_type}", method = RequestMethod.POST)
    @ResponseBody
    public Map postGroup(@PathVariable("org_id") final String org_id, @PathVariable("db_type") final String db_type,
            @RequestBody Group model) {
        
        model.setDbType(db_type);
        model.setOrgId(org_id);
        
        return orgService.insertGroup(model, null);
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 수정
     * </pre>
     * 
     * @Method Name : putGroup
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param group_id
     * @param db_type
     * @param body
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{group_id}/{db_type}", method = RequestMethod.PUT)
    public Map<String, String> putGroup(@PathVariable("org_id") final String org_id,
            @PathVariable("group_id") final String group_id, @PathVariable("db_type") final String db_type,
            @RequestBody Group model) {

        model.setDbType(db_type);
        model.setOrgId(org_id);
        model.setId(group_id);
        
        return orgService.updateGroup(model);
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 삭제
     * </pre>
     * 
     * @Method Name : deleteGruop
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param group_id
     * @param db_type
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{group_id}/{db_type}", method = RequestMethod.DELETE)
    public Map deleteGroup(@PathVariable("org_id") final String org_id, @PathVariable("group_id") final String group_id,
            @PathVariable("db_type") final String db_type) {

        Group model = new Group();
        model.setDbType(db_type);
        model.setOrgId(org_id);
        model.setId(group_id);

        return orgService.deleteGroup(model);
    }

    /**
     * <pre>
     * desc : 조직변경 Logs
     * </pre>
     * 
     * @Method Name : emitLogs
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @return
     */
    @RequestMapping(value = "/org-chart/{org_id}/status/{db_type}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> emitLogs(@PathVariable("org_id") final String org_id, @PathVariable("db_type") final String db_type) {
    	
    	Map<String, Object> responseMap = new HashMap<String, Object>();

		String result = null;
		if(rabbitMQService != null)
			result = rabbitMQService.requestQueue(db_type, org_id);
		
		if(result == null)
			result = "NO_CHANGES";
		
		System.out.println("---------------->"+ result);
		responseMap.put("status",result );
	
		return responseMap;
    	
    }

    /**
     * <pre>
     * desc : 그룹(하위조직) 등록 with File
     * </pre>
     * 
     * @Method Name : postGroup
     * @date : 2015. 11. 3.
     * @author : openPaaS
     * 
     * @param org_id
     * @param db_type
     * @param map
     * @return
     */
    @RequestMapping(value = "/orgs/{org_id}/groups/{db_type}/file", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Map postGroup(@PathVariable("org_id") final String org_id, @PathVariable("db_type") final String db_type,
            @RequestParam("file") MultipartFile file) {

        Group model = new Group();
        model.setDbType(db_type);
        model.setOrgId(org_id);

        return orgService.insertGroup(model, file);
    }
}
