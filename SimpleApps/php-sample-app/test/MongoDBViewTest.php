<?php
require dirname(__FILE__).'/../api/mongodb_view.php';

class MongoDBViewTest extends \PHPUnit_Framework_TestCase {

    private $org_id   = '5667e77c897caa188b000029';   
    private $group_id = '5667ecb2897caad883000029';
    private $parent_id = "";
    
    protected $backupGlobals = false;
    
//    public function testInsertOrg() {
    public function InsertOrg() {
        $body = array(
            "label" => "php test",
            "desc" => "php test txt",
            "url" => "http://www.naver.com"
        );
        
        $view = new MongoDBView();    
        $result = $view->insertOrg($body);        
        
        echo 'result:'.json_encode($result).'               ';
  
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }

//    public function testInsertGroup() {
    public function InsertGroup() {
        $body = array(
            "org_id" => $this->org_id,
            "parent_id" => $this->parent_id,
            "label" => "php group label",
            "desc" => "php group desc",
            "thumb_img_name" => "abc.jpg",
            "thumb_img_path" => "http://abc.jpg",
            "url" => "http://www.naver.com",
        );
        
        $view = new MongoDBView();    
        $result = $view->insertGroup($this->org_id, $body);        
        
        echo 'result:'.json_encode($result).'               ';
  
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }
    
    
    public function testGetOrgChart() {
        
        $view = new MongoDBView();    
        $result = $view->getOrgChart($this->org_id);        
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(true);
    }

    public function testGetOrgs() {

        $view = new MongoDBView();    
        $result = $view->getOrgs();
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }    

    public function testGetOrg() {
        
        $view = new MongoDBView();    
        $result = $view->getOrg($this->org_id);
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }
    
    public function testUpdateOrg() {
        $body = array(
            "label" => "php test ".time(),
            "desc" => "php test txt ".time(),
            "url" => "http://www.naver.com "
        );
        
        $view = new MongoDBView();    
        $result = $view->updateOrg($this->org_id, $body);
        
        echo 'result:'.json_encode($result).'               ';
        
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }
    
    public function DeleteOrg() {
//    public function testDeleteOrg() {
        if ($this->org_id <= 0) {
            $this->assertTrue(FALSE);
            return;
        }
        
        $view = new MongoDBView();    
        $result = $view->deleteOrg($this->org_id);
        
        echo 'result:'.json_encode($result).'               ';
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }    

    public function testGetGroups() {
        $view = new MongoDBView();    
        $result = $view->getGroups($this->org_id);
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }
    
    public function testGetGroup() {
        $view = new MongoDBView();    
        $result = $view->getGroup($this->org_id, $this->group_id);
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }
    
    public function testUpdateGroup() {
        $body = array(
            "org_id" => $this->org_id,
            "parent_id" => $this->parent_id,
            "label" => "php group label ".time(),
            "desc" => "php group desc ".time(),
            "thumb_img_name" => "abc.jpg ".time(),
            "thumb_img_path" => "http://abc.jpg",
            "url" => "http://www.naver.com ".time()
        );
        
        $view = new MongoDBView();    
        $result = $view->updateGroup($this->org_id, $this->group_id, $body);
        
        echo 'result:'.json_encode($result).'               ';
        
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }
    
    public function DeleteGroup() {
//    public function testDeleteGroup() {
        if ($this->org_id <= 0) {
            $this->assertTrue(FALSE);
            return;
        }
        
        $view = new MongoDBView();    
        $result = $view->deleteGroup($this->org_id, $this->group_id);
        
        echo 'result:'.json_encode($result).'               ';
        
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }  
        
}
?>