package sample.biz;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import sample.models.Group;
import sample.models.Organization;

public interface OrganizationService {
	
	public Map selectOrgGroups(Organization model);

    public Map selectOrganizations(Organization model);
    public Map selectOrganization(Organization model);
    public Map insertOrganization(Organization model);
    public Map updateOrganization(Organization model);
    public Map deleteOrganization(Organization model);

    public Map selectGroups(Group model);
    public Map selectGroup(Group model);
    public Map insertGroup(Group model, MultipartFile uploadedFile);
    public Map updateGroup(Group model);
    public Map deleteGroup(Group model);
    
}


