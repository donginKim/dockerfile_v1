package sample.dao.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import sample.models.Organization;

//@Repository("mongoRepository")
public interface OrganizationMongoRepository extends MongoRepository<Organization, String>, OrganizationRepository {

}
