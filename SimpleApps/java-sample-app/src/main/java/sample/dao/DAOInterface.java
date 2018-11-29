package sample.dao;

import java.util.List;
import java.util.Map;

import sample.models.Group;
import sample.models.Organization;

public interface DAOInterface {

    public abstract List selectOrganizations(Organization model);
    public abstract Map selectOrganization(Organization model);
    public abstract int insertOrganization(Organization model);
    public abstract int updateOrganization(Organization model);
    public abstract int deleteOrganization(Organization model);
    public long selectMaxOrganizationId(Organization param);
    public Map selectOrganizationId(Organization param);
    
    public abstract List selectGroups(Group model);
    public abstract Map selectGroup(Group model);
    public abstract int insertGroup(Group model);
    public abstract int updateGroup(Group model);
    public abstract int deleteGroup(Group model);
    
}


