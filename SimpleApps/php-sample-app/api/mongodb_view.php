<?php
require  dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require  dirname(__FILE__).'/../vendor/autoload.php';

class MongoDBView {
    
    private $host;
    private $username;
    private $password;
    private $dbname;
    private $uri;
    
    function MongoDBView() {        

        if (array_key_exists("VCAP_SERVICES", $_ENV)) {

	    $env = json_decode($_ENV["VCAP_SERVICES"], $assoc=true);
            
            $credentials = $env["Mongo-DB"][0]["credentials"];            
            
            $this->host = $credentials["hosts"][0];

            // 현재 PHP Buildpack에서는 DB Auth가 안되고 있음. 그래서 일단 root 계정으로 접속하는 예제로 만들었음.
//            $this->username = $credentials["username"];
//            $this->password = $credentials["password"];
            $this->username = 'root';
            $this->password = 'openpaas';
            
            $this->dbname = $credentials["name"];
//            $this->uri = $credentials["uri"];
            $this->uri = 'mongodb://root:openpaas@'.$this->host;

        } else {

          // Local에서 개발을 하기 위한 Connction 정보
	  // Ternel을 이용하여 접속  
            $this->host = 'localhost:51013';
//            $this->username = 'd82a9776-5864-447d-8fea-bcf424451961';
//            $this->password = '02351bd2-bb15-43e8-acdd-64c25b42d2a4';
//            $this->dbname = 'e37e541c-75de-4f01-8196-63e2d902e768';

            // 현재 PHP Buildpack에서는 DB Auth가 안되고 있음. 그래서 일단 root 계정으로 접속하는 예제로 만들었음.
            $this->username = 'root';
            $this->password = 'openpaas';
            $this->dbname = 'e37e541c-75de-4f01-8196-63e2d902e768';
            
            $this->uri = 'mongodb://root:openpaas@localhost:51013';
            $this->uri2 = 'mongodb://localhost:51013';
            
        }

    }    

    // Org, Group 정보를 조회
    public function getOrgChart($org_id) {        

        // Result할 객체 
        $result = array(
            "org" => array(),
            "groups" => array()
        );
        
        $mongo = new MongoClient($this->uri);       
//        $mongo = new MongoClient($this->uri, array('authSource'=>$this->dbname, 'db'=>$this->dbname, 'username'=>$this->username, 'password'=>$this->password));       
//        $mongo = new MongoClient($this->uri2, array('authMechanism'=>'SCRAM-SHA-1', 'authSource'=>$this->dbname, 'db'=>$this->dbname, 'username'=>$this->username, 'password'=>$this->password));       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        $cursor = $collection->find(array('_id'=>new MongoId($org_id)));
        
        foreach ($cursor as $row) {
            //echo json_encode($document)."\n";
            
            $org = array(
                "id" => strval($row["_id"]),
                "label" => isset($row["label"]) ? $row["label"] : "",
                "desc" => isset($row["desc"]) ? $row["desc"] : "",
                "url" => isset($row["url"]) ? $row["url"] : "",
                "created" => isset($row["created"]) ? $row["created"] : "",
                "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            $result["org"] = $org;            
        }
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        $cursor = $collection->find(array('org_id'=>new MongoId($org_id)));
        
        foreach ($cursor as $row) {
            //echo json_encode($document)."\n";
            
            $group = array(
                    "id" => strval($row["_id"]),
                    "org_id" => strval($row["org_id"]),
                    "parent_id" => strval($row["parent_id"]),
                    "label" => isset($row["label"]) ? $row["label"] : "",
                    "desc" => isset($row["desc"]) ? $row["desc"] : "",
                    "thumb_img_name" => isset($row["thumb_img_name"]) ? $row["thumb_img_name"] : "",
                    "thumb_img_path" => isset($row["thumb_img_path"]) ? $row["thumb_img_path"] : "",
                    "url" => isset($row["url"]) ? $row["url"] : "",
                    "created" => isset($row["created"]) ? $row["created"] : "",
                    "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            array_push($result["groups"], $group);  
        }   
        
        $mongo->close();
        
        return $result;
    }

    // Org들의 정보를 조회
    public function getOrgs() {
        // Result할 객체 
        $result = array(
            "orgs" => array()
        );

        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        $cursor = $collection->find();
        
        foreach ($cursor as $row) {
            //echo json_encode($document)."\n";
            
            $org = array(
                "id" => strval($row["_id"]),
                "label" => isset($row["label"]) ? $row["label"] : "",
                "desc" => isset($row["desc"]) ? $row["desc"] : "",
                "url" => isset($row["url"]) ? $row["url"] : "",
                "created" => isset($row["created"]) ? $row["created"] : "",
                "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            array_push($result["orgs"], $org);
        }
        
        $mongo->close();
        
        return $result;
    } 

    // Org의 정보를 조회(상세조회)
    public function getOrg($org_id) {

        // Result할 객체 
        $result = array(
            "org" => array()
        );        
        
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        $cursor = $collection->find(array('_id'=>new MongoId($org_id)));
        
        foreach ($cursor as $row) {
            //echo json_encode($document)."\n";
            
            $org = array(
                "id" => strval($row["_id"]),
                "label" => isset($row["label"]) ? $row["label"] : "",
                "desc" => isset($row["desc"]) ? $row["desc"] : "",
                "url" => isset($row["url"]) ? $row["url"] : "",
                "created" => isset($row["created"]) ? $row["created"] : "",
                "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            $result["org"] = $org;            
        }

        $mongo->close();
        
        return $result;
    }    
    
    // Org 등록
    public function insertOrg($body) {
        $result = array();
        
        // ORG 정보 등록        
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        $id = new MongoId();
        $body["_id"] = $id;
        
        $insert = $collection->insert($body);
        
        if ($insert) {
            
            $result = array(
                "id" => strval($id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }  
    
    // Org 수정
    public function updateOrg($org_id, $body) {
        
        $result = array();
        
        // ORG 정보 등록
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        
        $insert = $collection->update(array("_id"=>new MongoId($org_id)), $body);
        
        if ($insert) {
            
            $result = array(
                "id" => strval($org_id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }
    
    // Org 삭제
    public function deleteOrg($org_id) {

        $result = array();
        
        // ORG 정보 삭제
        // ORG 정보 등록
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'ORG_TBL');
        
        $insert = $collection->remove(array("_id"=>new MongoId($org_id)));
        
        if ($insert) {
            
            $result = array(
                "id" => strval($org_id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }  
    
    // Group들의 정보 조회
    public function getGroups($org_id) {        
        
        // Result할 객체 
        $result = array(
            "groups" => array()
        );

        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        $cursor = $collection->find(array('org_id'=>new MongoId($org_id)));
        
        foreach ($cursor as $row) {
            //echo json_encode($document)."\n";
            
            $group = array(
                    "id" => strval($row["_id"]),
                    "org_id" => strval($row["org_id"]),
                    "parent_id" => strval($row["parent_id"]),
                    "label" => isset($row["label"]) ? $row["label"] : "",
                    "desc" => isset($row["desc"]) ? $row["desc"] : "",
                    "thumb_img_name" => isset($row["thumb_img_name"]) ? $row["thumb_img_name"] : "",
                    "thumb_img_path" => isset($row["thumb_img_path"]) ? $row["thumb_img_path"] : "",
                    "url" => isset($row["url"]) ? $row["url"] : "",
                    "created" => isset($row["created"]) ? $row["created"] : "",
                    "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            array_push($result["groups"], $group);  
        }   
        
        $mongo->close();
        
        return $result;
    }

    // Group의 정보 조회
    public function getGroup($org_id, $group_id) {        
        
        // Result할 객체 
        $result = array(
            "group" => array()
        );
        
        // Group 정보를 조회        
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        $cursor = $collection->find(array('org_id'=>new MongoId($org_id), "_id"=>new MongoId($group_id)));
        
        foreach ($cursor as $row) {
            
            $group = array(
                    "id" => strval($row["_id"]),
                    "org_id" => strval($row["org_id"]),
                    "parent_id" => strval($row["parent_id"]),
                    "label" => isset($row["label"]) ? $row["label"] : "",
                    "desc" => isset($row["desc"]) ? $row["desc"] : "",
                    "thumb_img_name" => isset($row["thumb_img_name"]) ? $row["thumb_img_name"] : "",
                    "thumb_img_path" => isset($row["thumb_img_path"]) ? $row["thumb_img_path"] : "",
                    "url" => isset($row["url"]) ? $row["url"] : "",
                    "created" => isset($row["created"]) ? $row["created"] : "",
                    "modified" => isset($row["modified"]) ? $row["modified"] : ""
            );
            array_push($result["group"], $group);  
        }   
        
        $mongo->close();        
        
        return $result;
    }
    
    // Group 등록
    public function insertGroup($org_id, $body) {
        $result = array();
        
        // ORG 정보 등록
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        $id = new MongoId();
        $body["_id"] = $id;
        $body["org_id"] = new MongoId($body["org_id"]);
        
        $insert = $collection->insert($body);
        
        if ($insert) {
            
            $result = array(
                "id" => strval($id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }

    // Group 수정
    public function updateGroup($org_id, $group_id, $body) {
        
        $result = array();
        
        // Group 정보 등록
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        $body["org_id"] = new MongoId($body["org_id"]);
        
        $insert = $collection->update(array("org_id"=>new MongoId($org_id), "_id"=>new MongoId($group_id)), $body);
        
        if ($insert) {
            
            $result = array(
                "id" => strval($group_id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }
    
    // Group 삭제
    public function deleteGroup($org_id, $group_id) {
        
        $result = array();
        
        // GROUP 정보 삭제
        $mongo = new MongoClient($this->uri);       
        
        $collection = $mongo->selectCollection($this->dbname, 'GROUP_TBL');
        
        $insert = $collection->remove(array("org_id"=>new MongoId($org_id), "_id"=>new MongoId($group_id)));
        
        if ($insert) {
            
            $result = array(
                "id" => strval($group_id)
            );
                
        } else {
            $result = array(
                "RESULT" => "ERROR"
            );
        }
            
        $mongo->close();
        
        return $result;
    }    
}
?>

