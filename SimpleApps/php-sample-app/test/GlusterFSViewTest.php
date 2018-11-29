<?php
require dirname(__FILE__).'/../api/glusterfs_view.php';

class GlusterFSViewTest extends \PHPUnit_Framework_TestCase {

    protected $backupGlobals = false;

    // 이미지 Upload 테스트
//    public function testUploadFile() {
    public function UploadFile() {
        
        $view = new GlusterFSView();    
        $result = $view->uploadFile('./resources/assets/images/images.png', 'test.png');
        
        echo 'result:'.json_encode($result).'               ';
        
        $this->assertTrue(true);
    }

}
?>