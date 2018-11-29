<?php
require  dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require  dirname(__FILE__).'/../vendor/autoload.php';

class MySQLView {
    
    private $host;
    private $username;
    private $password;
    private $dbname;
    
    function MySQLView() {        

        if (array_key_exists("VCAP_SERVICES", $_ENV)) {

	       $env = json_decode($_ENV["VCAP_SERVICES"], $assoc=true);

           $this->host = $env["Mysql-DB"][0]["credentials"]["hostname"].':'.$env["Mysql-DB"][0]["credentials"]["port"];
           $this->username = $env["Mysql-DB"][0]["credentials"]["username"];
           $this->password = $env["Mysql-DB"][0]["credentials"]["password"];
           $this->dbname = $env["Mysql-DB"][0]["credentials"]["name"];

        } else {

          // Local에서 개발을 하기 위한 Connction 정보
	  // Ternel을 이용하여 접속  
          $this->host = 'localhost:51010';
//        $this->host = '10.30.40.63:3306';
          $this->username = '5TmeRjRIQNbwcWyq';
          $this->password = 'cvith7XNRMU5nLMI';
          $this->dbname = 'cf_ea68784e_3de6_439d_afc1_d51b4e95627b';

        }

//        echo 'host:'.$this->host.', username:'.$this->username.', password:'.$this->password.', dbname:'.$this->dbname;

    }    

    // Org, Group 정보를 조회
    public function getOrgChart($org_id) {        
        
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        // ORG 정보를 조회
        $sql = "SELECT * FROM ORG_TBL WHERE id = ".$org_id;
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $org_result = $stmt->get_result();
        
        // Result할 객체 
        $result = array(
            "org" => array(),
            "groups" => array()
        );

        if ($org_result->num_rows > 0) {
            // output data of each row
            $row = $org_result->fetch_assoc();
            
            $org = array(
                "id" => strval($row["id"]),
                "label" => $row["label"],
                "desc" => $row["desc"],
                "url" => $row["url"],
                "created" => $row["created"],
                "modified" => $row["modified"]
            );
            $result["org"] = $org;
        }
        
        // Group 정보를 조회        
        $sql = "SELECT * FROM GROUP_TBL WHERE org_id = ".$org_id;
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $group_result = $stmt->get_result();                
        
        
        if ($group_result->num_rows > 0) {
            // output data of each row
            while($row = $group_result->fetch_assoc()) {
                
                $group = array(
                    "id" => strval($row["id"]),
                    "org_id" => strval($row["org_id"]),
                    "parent_id" => strval($row["parent_id"]),
                    "label" => $row["label"],
                    "desc" => $row["desc"],
                    "thumb_img_name" => $row["thumb_img_name"],
                    "thumb_img_path" => $row["thumb_img_path"],
                    "url" => $row["url"],
                    "created" => $row["created"],
                    "modified" => $row["modified"]
                );
                
                array_push($result["groups"], $group);                
            }            
        }        
        
        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Org들의 정보를 조회
    public function getOrgs() {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        // ORG 정보를 조회
        $sql = "SELECT * FROM ORG_TBL";
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $org_result = $stmt->get_result();
        
        // Result할 객체 
        $result = array(
            "orgs" => array()
        );
        
        
        if ($org_result->num_rows > 0) {
            // output data of each row
            while($row = $org_result->fetch_assoc()) {
                
                $org = array(
                    "id" => strval($row["id"]),
                    "label" => $row["label"],
                    "desc" => $row["desc"],
                    "url" => $row["url"],
                    "created" => $row["created"],
                    "modified" => $row["modified"]
                );
                
                array_push($result["orgs"], $org);
            }            
        }        
        
        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Org의 정보를 조회(상세조회)
    public function getOrg($org_id) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        // ORG 정보를 조회
        $sql = "SELECT * FROM ORG_TBL WHERE id = ".$org_id;
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $org_result = $stmt->get_result();
        
        // Result할 객체 
        $result = array(
            "org" => array()
        );        
        
        if ($org_result->num_rows > 0) {
            // output data of each row
            $row = $org_result->fetch_assoc();
                
            $org = array(
                "id" => strval($row["id"]),
                "label" => $row["label"],
                "desc" => $row["desc"],
                "url" => $row["url"],
                "created" => $row["created"],
                "modified" => $row["modified"]
            );
                
            array_push($result["org"], $org);
        }  
        
        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Org 등록
    public function insertOrg($body) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // ORG 정보 등록
        $sql = 'INSERT INTO ORG_TBL (label, `desc`, url) VALUES (?,?,?)';
        
        if ($stmt = $conn->prepare($sql)) {
            $stmt->bind_param("sss", $body["label"], $body["desc"], $body["url"]);

            if ($stmt->execute()) {
                
                // Result할 객체 
                $result = array(
                    "id" => strval($stmt->insert_id)
                );

                
            } else {
                $result = array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Org 수정
    public function updateOrg($org_id, $body) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // ORG 정보 등록
        $sql = 'UPDATE ORG_TBL SET label = ?, `desc` = ?, url = ? WHERE id = ?';
        
        if ($stmt = $conn->prepare($sql)) {
            $stmt->bind_param("sssi", $body["label"], $body["desc"], $body["url"], $org_id);

            if ($stmt->execute()) {
                
                // Result할 객체 
                $result = array(
                    "id" => strval($org_id)
                );
                
            } else {
                $result = array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Org 삭제
    public function deleteOrg($org_id) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // ORG 정보 삭제
        $sql = 'DELETE FROM ORG_TBL WHERE id = ?';
        
        if ($stmt = $conn->prepare($sql)) {
            $stmt->bind_param("i", $org_id);

            if ($stmt->execute()) {
                
                // Result할 객체 
                $result = array(
                    "id" => strval($org_id)
                );
                
            } else {
                $result = array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Group들의 정보 조회
    public function getGroups($org_id) {        
        
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        
        // Result할 객체 
        $result = array(
            "groups" => array()
        );
        // Group 정보를 조회        
        $sql = "SELECT * FROM GROUP_TBL WHERE org_id = ".$org_id;
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $group_result = $stmt->get_result();        
        
        if ($group_result->num_rows > 0) {
            // output data of each row
            while($row = $group_result->fetch_assoc()) {
                
                $group = array(
                    "id" => strval($row["id"]),
                    "org_id" => strval($row["org_id"]),
                    "parent_id" => strval($row["parent_id"]),
                    "label" => $row["label"],
                    "desc" => $row["desc"],
                    "thumb_img_name" => $row["thumb_img_name"],
                    "thumb_img_path" => $row["thumb_img_path"],
                    "url" => $row["url"],
                    "created" => $row["created"],
                    "modified" => $row["modified"]
                );
                
                array_push($result["groups"], $group);                
            }            
        }        
        
        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Group의 정보 조회
    public function getGroup($org_id, $group_id) {        
        
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        
        // Result할 객체 
        $result = array(
            "group" => array()
        );
        
        // Group 정보를 조회        
        $sql = "SELECT * FROM GROUP_TBL WHERE org_id = ".$org_id." AND id = ".$group_id;
        $stmt = $conn->prepare($sql);
        
        $stmt->execute();
        $group_result = $stmt->get_result();        
        
        if ($group_result->num_rows > 0) {
            // output data of each row
            $row = $group_result->fetch_assoc();
                
            $group = array(
                "id" => strval($row["id"]),
                "org_id" => strval($row["org_id"]),
                "parent_id" => strval($row["parent_id"]),
                "label" => $row["label"],
                "desc" => $row["desc"],
                "thumb_img_name" => $row["thumb_img_name"],
                "thumb_img_path" => $row["thumb_img_path"],
                "url" => $row["url"],
                "created" => $row["created"],
                "modified" => $row["modified"]
            );
                
            array_push($result["group"], $group);                
        }        
        
        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Group 등록
    public function insertGroup($org_id, $body) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // ORG 정보 등록
        $sql = 'INSERT INTO GROUP_TBL (org_id, parent_id, label, `desc`, thumb_img_name, thumb_img_path, url) '.
                'VALUES  (?,?,?,?,?,?,?)';
        
        if ($stmt = $conn->prepare($sql)) {
            if ($body["parent_id"] == "") {
                $body["parent_id"] = null;
            } else {
                $body["parent_id"] = intval($body["parent_id"]);
            }
            
            $stmt->bind_param("iisssss", intval($body["org_id"]), $body["parent_id"], 
                                        $body["label"], $body["desc"], $body["thumb_img_name"],
                                        $body["thumb_img_path"], $body["url"]);

            if ($stmt->execute()) {
                // Result할 객체 
                $result = array(
                    "id" => strval($stmt->insert_id)
                );
                
            } else {
                var_dump($stmt->error);
                $result =  array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Group 수정
    public function updateGroup($org_id, $group_id, $body) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // ORG 정보 등록
        $sql = 'UPDATE GROUP_TBL SET '.
                    'org_id = ?, parent_id = ?, label = ?, '.
                    '`desc` = ?, thumb_img_name = ?, thumb_img_path = ?, url = ? WHERE id = ?';
        
        if ($stmt = $conn->prepare($sql)) {
            if ($body["parent_id"] == "") {
                $body["parent_id"] = null;
            } else {
                $body["parent_id"] = intval($body["parent_id"]);
            }
            $stmt->bind_param("iisssssi", intval($body["org_id"]), $body["parent_id"], $body["label"], 
                              $body["desc"], $body["thumb_img_name"], $body["thumb_img_path"], 
                              $body["url"], $group_id);

            if ($stmt->execute()) {
                
                // Result할 객체 
                $result = array(
                    "id" => strval($group_id)
                );
                
            } else {
                $result = array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }

    // Group 삭제
    public function deleteGroup($org_id, $group_id) {
        $conn = new mysqli($this->host, $this->username, $this->password, $this->dbname);
        
        if($conn->connect_error) {
            die("conncetion failed:".$conn->connect_error);            
        }
        
        $result = array();
        
        // GROUP 정보 삭제
        $sql = 'DELETE FROM GROUP_TBL WHERE id = ?';
        
        if ($stmt = $conn->prepare($sql)) {
            $stmt->bind_param("i", $group_id);

            if ($stmt->execute()) {
                
                // Result할 객체 
                $result = array(
                    "id" => strval($group_id)
                );
                
            } else {
                $result = array(
                    "RESULT" => "ERROR"
                );
            }
            
        } else {
            var_dump($conn->error);
            $result = array(
                "RESULT" => "ERROR"
            );
            
            return $result;
        }

        $stmt->close();
        $conn->close();
        
        return $result;
    }
}
?>
