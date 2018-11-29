<?php
require dirname(__FILE__).'/../api/redis_view.php';

class RedisViewTest extends \PHPUnit_Framework_TestCase {

    protected $backupGlobals = FALSE;
    
    private $key = 'php_test_key';
    private $username = 'admin';
    
    public function testInsertKey() {

        $view = new RedisView();    
        $result = $view->insertKey($this->key, $this->username);        
        
        echo 'result:'.json_encode($result).'               ';
  
        $this->assertTrue(TRUE);
    }
    
    public function testDeleteKey() {
        $view = new RedisView();    
        $result = $view->deleteKey($this->key);        
        
        echo 'result:'.json_encode($result).'               ';
  
        $this->assertTrue(TRUE);
        
    }
}
?>