package sample.dao.mongodb;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sample.models.Group;
import sample.models.MongoGroup;

public interface GroupRepository extends CrudRepository<MongoGroup, String> 
{
	
    //public List<Group> findAllOrg();
}
