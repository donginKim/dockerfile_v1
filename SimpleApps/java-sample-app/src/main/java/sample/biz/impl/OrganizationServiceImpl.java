package sample.biz.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.bson.types.ObjectId;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;

import sample.biz.OrganizationService;
import sample.biz.RabbitMQService;
import sample.dao.DAOInterface;
import sample.dao.cubrid.CubridDAO;
import sample.dao.mongodb.GroupMongoRepository;
import sample.dao.mongodb.OrganizationMongoRepository;
import sample.dao.mysql.MysqlDAO;
import sample.models.Group;
import sample.models.MongoGroup;
import sample.models.Organization;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

	String RABITMQ_URL = "localhost";
	String RABITMQ_QUEUE_NAME = "task_queue";

	@Autowired
	private CubridDAO cubridDAO;

	@Autowired
	private MysqlDAO mysqlDAO;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired
	private OrganizationMongoRepository mongoOrgDAO;

	@Autowired
	private GroupMongoRepository mongoGroupDAO;

	@Autowired(required = false)
	@Qualifier("rabbitMQService")
	RabbitMQService rabbitMQService;

	private final static String DB_TYPE_MYSQL = "mysql";
	private final static String DB_TYPE_CUBRID = "cubrid";
	private final static String DB_TYPE_MONGO = "mongo";

	private final String NO_CHANGES = "NO_CHANGES";
	private final String ORG_DELETED = "ORG_DELETED";
	private final String ORG_UPDATED = "ORG_UPDATED";
	private final String GROUP_ADDED = "GROUP_ADDED";
	private final String GROUP_DELETED = "GROUP_DELETED";
	private final String GROUP_UPDATED = "GROUP_UPDATED";

	private DAOInterface makeRepository(Map model) {
		if (model.containsKey("db_type")) {

			if (DB_TYPE_MYSQL.equals(model.get("db_type"))) {
				return mysqlDAO;
			}

			if (DB_TYPE_CUBRID.equals(model.get("db_type"))) {
				return cubridDAO;
			}

			// if(DB_TYPE_MONGO.equals(model.get("db_type"))){
			// return mongoDAO;
			// }
		} else {
			return null;
		}
		return null;
	}

	private DAOInterface makeRepository(Organization model) {
		if (!"".equals(model.getDbType())) {

			if (DB_TYPE_MYSQL.equals(model.getDbType())) {
				return mysqlDAO;
			}

			if (DB_TYPE_CUBRID.equals(model.getDbType())) {
				return cubridDAO;
			}

			// if(DB_TYPE_MONGO.equals(model.get("db_type"))){
			// return mongoDAO;
			// }
		} else {
			return null;
		}
		return null;
	}

	private DAOInterface makeRepository(Group model) {
		if (!"".equals(model.getDbType())) {

			if (DB_TYPE_MYSQL.equals(model.getDbType())) {
				return mysqlDAO;
			}

			if (DB_TYPE_CUBRID.equals(model.getDbType())) {
				return cubridDAO;
			}

			// if(DB_TYPE_MONGO.equals(model.get("db_type"))){
			// return mongoDAO;
			// }
		} else {
			return null;
		}
		return null;
	}

	@Override
	public Map selectOrgGroups(Organization model) {
		DAOInterface repository = this.makeRepository(model);
		Map orgMap = null;
		List groupList = null;

		if (repository != null) {
			orgMap = repository.selectOrganization(model);

			Group group = new Group();
			group.setOrgId(model.getId());
			groupList = repository.selectGroups(group);

		} else {

			Organization org = mongoOrgDAO.findOne(model.getId());

			if (org != null) {

				Query query = new Query();
				ObjectId objID = new ObjectId(model.getId());
				query.addCriteria(Criteria.where("orgId").is(objID));

				groupList = mongoTemplate.find(query, Group.class);

				// mongoDb는 ParentId로 되어 있기 때문에
				// UI로 전송을 위해 Parent_id로 세탱해준다.
				for (int i = 0; i < groupList.size(); i++) {
					Group grp = (Group) groupList.get(i);
					grp.setParent_id(grp.getParentId());
					grp.setThumb_img_path(grp.getThumbImgPath());
					grp.setThumb_img_name(grp.getThumbImgName());
					groupList.set(i, grp);
				}

				groupList = groupList;
				orgMap = org.toMap();

			} else {
				orgMap = new HashMap();
				groupList = new ArrayList();
			}
		}

		Map map = new HashMap();
		map.put("org", orgMap);
		map.put("groups", groupList);

		return map;
	}

	@Override
	public Map selectOrganizations(Organization model) {
		List list = null;

		DAOInterface repository = this.makeRepository(model);

		if (repository != null) {
			list = repository.selectOrganizations(model);
		} else {
			list = mongoOrgDAO.findAll();
			Query query = new Query();

			// query.addCriteria(Criteria.where("label").is(15));
			// List xxx = mongoTemplate.find(query, Organization.class);
		}

		Map map = new HashMap();
		map.put("orgs", list);

		return map;
	}

	@Override
	public Map selectOrganization(Organization model) {

		DAOInterface repository = this.makeRepository(model);
		Map orgMap = null;

		if (repository != null) {
			orgMap = repository.selectOrganization(model);
		} else {
			Organization org = mongoOrgDAO.findOne(model.getId());
			orgMap = org.toMap();
		}

		Map map = new HashMap();
		map.put("org", orgMap);
		return map;
	}

	@Override
	public Map insertOrganization(Organization model) {

		DAOInterface repository = this.makeRepository(model);
		int count = 0;
		long insertOrgId = 0;
		if (repository != null) {
			count = repository.insertOrganization(model);
			Map orgInfo = repository.selectOrganizationId(model);
			insertOrgId = (long) orgInfo.get("id");

		} else {
			long orgCnt = mongoOrgDAO.count();
			model.setCreated((new Date()).toString());
			model.setModified((new Date()).toString());
			
			Organization org = mongoOrgDAO.save(model);
			count = (org == null) ? 0 : 1;
		}

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("count", count);

		rabbitMQService.createQueue(model.getDbType(), String.valueOf(insertOrgId));

		return map;
	}

	@Override
	public Map updateOrganization(Organization model) {

		DAOInterface repository = this.makeRepository(model);
		int count = 0;
		if (repository != null) {
			count = repository.updateOrganization(model);
		} else {
			// model.put("modified", new Date());
			//MongoGroup mgGroup = new MongoGroup(model);
			model.setModified((new Date()).toString());
			Organization org = mongoOrgDAO.save(model);
			count = (org == null) ? 0 : 1;
		}

		Map map = new HashMap();
		map.put("count", count);

		// save Queue
		rabbitMQService.inputQueue(model.getDbType(), model.getId(), ORG_UPDATED);

		return map;
	}

	@Override
	public Map<String, Integer> deleteOrganization(Organization model) {

		DAOInterface repository = this.makeRepository(model);
		int count = 0;
		if (repository != null) {
			count = repository.deleteOrganization(model);
		} else {
			mongoOrgDAO.delete(model.getId());
			count = 1;
		}

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("count", count);

		rabbitMQService.inputQueue(model.getDbType(), model.getId(), ORG_DELETED);

		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, List> selectGroups(Group model) {

		DAOInterface repository = this.makeRepository(model);
		List list = null;
		if (repository != null) {
			list = repository.selectGroups(model);
		} else {

			Query query = new Query();
			ObjectId objID = new ObjectId(model.getOrgId());
			query.addCriteria(Criteria.where("orgId").is(objID));
			list = mongoTemplate.find(query, Group.class);

			// mongoDb는 ParentId로 되어 있기 때문에
			// UI로 전송을 위해 Parent_id로 세탱해준다.
			for (int i = 0; i < list.size(); i++) {
				Group grp = (Group) list.get(i);
				grp.setParent_id(grp.getParentId());
				grp.setThumb_img_path(grp.getThumbImgPath());
				grp.setThumb_img_name(grp.getThumbImgName());
				list.set(i, grp);
			}

		}

		Map<String, List> map = new HashMap<String, List>();
		map.put("groups", list);
		return map;
	}

	@Override
	public Map selectGroup(Group model) {

		DAOInterface repository = this.makeRepository(model);
		Map groupMap = null;
		if (repository != null) {
			groupMap = repository.selectGroup(model);
		} else {
			Organization org = mongoOrgDAO.findOne(model.getOrgId());
			for (Group group : org.getGroups()) {
				if (group.getId().equals(model.getId())) {
					groupMap = group.toMap();
					break;
				}
			}
		}

		Map map = new HashMap();
		map.put("group", groupMap);
		return map;
	}

	@Override
	public Map insertGroup(Group model, MultipartFile uploadFile) {

		DAOInterface repository = this.makeRepository(model);
		Group groupIn = new Group(model);
		int count = 0;

		if (repository != null) {

			count = repository.insertGroup(model);

		} else {
			
			Organization org = mongoOrgDAO.findOne(model.getOrgId());
			
			groupIn.setCreated((new Date()).toString());
			groupIn.setModified((new Date()).toString());
			org.getGroups().add(groupIn);

			model.setOrgId(model.getOrgId() );
			// ObjectId objID = new ObjectId( model.getOrgId());
			MongoGroup mgGroup = new MongoGroup(model);
			mgGroup.setParentId(mgGroup.getParent_id());
			MongoGroup orgOut = mongoGroupDAO.save(mgGroup);

			count = (orgOut != null) ? 1 : 0;
		}

		// save file
		if (uploadFile != null) {

			// try {
			// saveFile(uploadFile);
			// } catch (IOException | URISyntaxException | InterruptedException
			// e) {
			// e.printStackTrace();
			// }
			System.out.println("saveFile working");
		}

		// save MQ
		rabbitMQService.inputQueue(model.getDbType(), groupIn.getOrgId(), GROUP_ADDED);
		Map map = new HashMap();
		map.put("count", count);

		return map;
	}

	private void saveFile(MultipartFile uploadedFile) throws IOException, URISyntaxException, InterruptedException {
		Properties properties = new Properties();
		properties.load(Example.class.getClassLoader().getResourceAsStream("example.properties"));
		/*
		 * glusterfs.server=${glusterfs.server}
		 * glusterfs.volume=${glusterfs.volume}
		 */

		String vagrantBox = properties.getProperty("glusterfs.server");
		String volname = properties.getProperty("glusterfs.volume");

		String testUri = "gluster://" + vagrantBox + ":" + volname + "/baz";

		// create testFile
		Path path = Paths.get(new URI(testUri));
		System.out.println(path.getClass());
		System.out.println(path);
		System.out.println(path.getFileSystem().toString());
		System.out.println(path.toString());

		FileSystem fileSystem = FileSystems.newFileSystem(new URI(testUri), null);
		FileStore store = fileSystem.getFileStores().iterator().next();
		System.out.println("TOTAL SPACE: " + store.getTotalSpace());
		System.out.println("USABLE SPACE: " + store.getUsableSpace());
		System.out.println("UNALLOCATED SPACE: " + store.getUnallocatedSpace());
		System.out.println(fileSystem.toString());

		Set<PosixFilePermission> posixFilePermissions = PosixFilePermissions.fromString("rw-rw-rw-");
		FileAttribute<Set<PosixFilePermission>> attrs = PosixFilePermissions.asFileAttribute(posixFilePermissions);

		Path glusterPath = fileSystem.getPath("/baz");
		try {
			Files.createFile(glusterPath, attrs);
			System.out.println("File created");
		} catch (IOException e) {
			System.out.println("File exists, created at " + Files.getLastModifiedTime(glusterPath));
		}

		String hello = "Hello, ";
		Files.write(glusterPath, uploadedFile.getBytes(), StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
		String world = "world!";
		Files.write(glusterPath, world.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);

		long bazSize = Files.size(glusterPath);
		System.out.println("SIZE: " + bazSize);

		byte[] readBytes = Files.readAllBytes(glusterPath);
		System.out.println(hello + world + " == " + new String(readBytes));
		System.out.println("Last modified: " + Files.getLastModifiedTime(glusterPath) + " (should be now)");

		fileSystem.close();
	}

	@Override
	public Map updateGroup(Group model) {

		DAOInterface repository = this.makeRepository(model);
		Group mapToGroup = new Group(model);
		int count = 0;

		if (repository != null) {
			count = repository.updateGroup(model);
		} else {

			MongoGroup mgGroup = new MongoGroup(model);
			MongoGroup grp = mongoGroupDAO.findOne(mgGroup.getId());
			grp.setOrgId(new ObjectId(mapToGroup.getOrgId()));
			grp.setParentId(mapToGroup.getParent_id());
			grp.setLabel(mapToGroup.getLabel());
			grp.setDesc(mapToGroup.getDesc());
			grp.setThumb_img_name(mapToGroup.getThumb_img_name());
			grp.setThumb_img_path(mapToGroup.getThumb_img_path());
			grp.setUrl(mapToGroup.getUrl());
			grp.setModified((new Date()).toString());
			
			MongoGroup orgOut  = mongoGroupDAO.save(grp);
			count = (orgOut != null) ? 1 : 0;
		}
		
		// save Queue
		rabbitMQService.inputQueue(model.getDbType(),model.getOrgId(),GROUP_UPDATED);

		Map map = new HashMap();
		map.put("count",count);

		return map;
	}



	

	@Override
	public Map deleteGroup(Group model) {

		DAOInterface repository = this.makeRepository(model);
		Group findGroup = null;
		int count = 0;

		if (repository != null) {
			count = repository.deleteGroup(model);
		} else {

			// Organization org = mongoOrgDAO.findOne(model.getOrgId());
			// for (Group grp : org.getGroups()) {
			// if (grp.getId().equals(model.getId())) {
			// findGroup = grp;
			// break;
			// }
			// }
			//
			// org.getGroups().remove(findGroup);
			// Organization orgOut = mongoOrgDAO.save(org);

			mongoGroupDAO.delete(model.getId());
			Organization orgOut = new Organization();
			count = (orgOut != null) ? 1 : 0;
		}

		// save Queue
		rabbitMQService.inputQueue(model.getDbType(), model.getOrgId(), GROUP_DELETED);
		// saveQueue(findGroup, "delete", model.getDbType());

		Map map = new HashMap();
		map.put("count", count);

		return map;
	}
}
