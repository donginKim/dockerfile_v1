package sample.dao.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import sample.models.mongoOrg;

public interface MongoDao extends MongoRepository<mongoOrg, String> {

}
