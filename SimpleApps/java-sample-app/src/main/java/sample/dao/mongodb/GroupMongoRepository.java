package sample.dao.mongodb;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import sample.models.Group;
import sample.models.MongoGroup;

//@Repository("mongoRepository")
public interface GroupMongoRepository extends MongoRepository<MongoGroup, String>, GroupRepository {
 
	
}
