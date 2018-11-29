package sample.dao.mongodb;

import org.springframework.data.repository.CrudRepository;

import sample.models.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, String> 
{
    
}
