<?php
require dirname(__FILE__).'/../api/rabbitmq_view.php';

class RabbitMQViewTest extends \PHPUnit_Framework_TestCase {

    protected $backupGlobals = FALSE;
    
    private $org_id = 30;
    private $db_type = 'mysql';
    private $message = 'UPDATE';
    
    public function testInsertQueue() {

        $view = new RabbitMQView();    
        $result = $view->insertQueue($this->org_id, $this->db_type, $this->message);        
        
        echo 'result:'.json_encode($result).'               ';
  
        if (isset($result["RESULT"])) {
            $this->assertTrue(FALSE);
        } else {
            $this->assertTrue(TRUE);
        }
    }

}
?>