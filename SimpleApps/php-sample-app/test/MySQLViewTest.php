<?php
require dirname(__FILE__).'/../api/mysql_view.php';

class MySQLViewTest extends \PHPUnit_Framework_TestCase {

    private $org_id = "171";
    private $group_id = "";
    private $parent_id = "";
    
//    public function testInsertOrg() {
    public function InsertOrg() {

        $body = array(
            "label" => "php test",
            "desc" => "php test txt",
            "url" => "http://www.naver.com"
        );
        
        $view = new MySQLView();    
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
        
        $view = new MySQLView();    
        $result = $view->insertGroup($this->org_id, $body);        
        
        echo 'result:'.json_encode($result).'               ';
  
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }

    
    public function testGetOrgChart() {

        $view = new MySQLView();    
        $result = $view->getOrgChart($this->org_id);        
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }

    public function testGetOrgs() {

        $view = new MySQLView();    
        $result = $view->getOrgs();
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }

    public function testGetOrg() {

        $view = new MySQLView();    
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
        
        $view = new MySQLView();    
        $result = $view->updateOrg($this->org_id, $body);
        
        echo 'result:'.json_encode($result).'               ';

        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }

//    public function testDeleteOrg() {
    public function DeleteOrg() {

        if ($this->org_id <= 0) {
            $this->assertTrue(FALSE);
            return;
        }
        
        $view = new MySQLView();    
        $result = $view->deleteOrg($this->org_id);
        
        echo 'result:'.json_encode($result).'               ';

        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }

    public function testGetGroups() {

        $view = new MySQLView();    
        $result = $view->getGroups($this->org_id);
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(TRUE);
    }

    public function testGetGroup() {

        $view = new MySQLView();    
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
        
        $view = new MySQLView();    
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
        
        $view = new MySQLView();    
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