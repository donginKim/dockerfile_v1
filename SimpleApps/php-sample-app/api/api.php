<?php
require dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require dirname(__FILE__).'/../vendor/autoload.php';

require './mysql_view.php';
require './rabbitmq_view.php';
require './redis_view.php';
require './mongodb_view.php';
require './glusterfs_view.php';

$app = new \Slim\Slim();
$app->config('debug', true);

// #2.2.1 조직 및 그룹(하위조직) 리스트 요청
$app->get('/org-chart/:org_id/:db_type', function($org_id, $db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->getOrgChart($org_id);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->getOrgChart($org_id);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.2 조직 리스트 요청
$app->get('/orgs/:db_type', function($db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->getOrgs();
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->getOrgs();        
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.3 조직 정보 요청
$app->get('/orgs/:org_id/:db_type', function($org_id, $db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->getOrg($org_id);    
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->getOrg($org_id);    
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.4 조직 등록
$app->post('/orgs/:db_type', function($db_type) use($app) {

    $body = json_decode($app->request->getBody(), $assoc = true);
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->insertOrg($body);    
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->insertOrg($body);    
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.5 조직 수정
$app->put('/orgs/:org_id/:db_type', function($org_id, $db_type) use($app) {

    $body = json_decode($app->request->getBody(), $assoc = true);
    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->updateOrg($org_id, $body);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->updateOrg($org_id, $body);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.6 조직 삭제
$app->delete('/orgs/:org_id/:db_type', function($org_id, $db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->deleteOrg($org_id);    
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->deleteOrg($org_id);    
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.7 그룹(하위조직) 리스트 요청
$app->get('/orgs/:org_id/groups/:db_type', function($org_id, $db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->getGroups($org_id);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->getGroups($org_id);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);    
});

// #2.2.8 그룹(하위조직) 정보 요청
$app->get('/orgs/:org_id/groups/:group_id/:db_type', function($org_id, $db_type) use($app) {

    $result; 
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->getGroup($org_id, $group_id);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->getGroup($org_id, $group_id);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.9.	그룹(하위조직) 등록
$app->post('/orgs/:org_id/groups/:db_type', function($org_id, $db_type) use($app) {

    $body = json_decode($app->request->getBody(), $assoc = true);
    $result = array();   
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();  
        $result = $view->insertGroup($org_id, $body);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->insertGroup($org_id, $body);
    } else {
        $app->status = "500";
    }      
    
    echo json_encode($result);
    
});

// #2.2.10	그룹(하위조직) 수정
$app->put('/orgs/:org_id/groups/:group_id/:db_type', function($org_id, $group_id, $db_type) use($app) {

    $body = json_decode($app->request->getBody(), $assoc = true);
    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->updateGroup($org_id, $group_id, $body);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->updateGroup($org_id, $group_id, $body);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
});

// #2.2.11	그룹(하위조직) 삭제
$app->delete('/orgs/:org_id/groups/:group_id/:db_type', function($org_id, $group_id, $db_type) use($app) {

    $result;
    
    if ($db_type == 'mysql') {
        $view = new MySQLView();    
        $result = $view->deleteGroup($org_id, $group_id);
    } else if ($db_type == 'cubrid') {
        // 현재 Buildpack에서는 지원하지 안함.
    } else if ($db_type == 'mongo') {
        $view = new MongoDBView();    
        $result = $view->deleteGroup($org_id, $group_id);
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
    
});

// #2.2.12 로그인
$app->post('/manage/login', function() use($app) {

    $key = "php-test-key";
    
    $body = json_decode($app->request->getBody(), $assoc = true);
    $result;
    $view = new RedisView();    
    if ($body["id"] == "admin" && $body["password"] == "admin") {
        $app->response->headers->set('Set-Cookie', $key);
        $result = $view->insertKey($key, $body["id"]);
    } else {
        $app->status = 401;
    }
    
    echo json_encode($result);
    
});

// #2.2.13 로그아웃
$app->post('/manage/logout', function() use($app) {

    $cookie = $app->request->headers->get('cookie');
    $result;
    
    $view = new RedisView();  
    $result = $view->deleteKey($cookie);
    
    echo json_encode($result);
        
});

// #2.2.14 이미지 업로드
$app->post('/upload/file', function() use($app) {

    $view = new GlusterFSView();    
    $result = $view->uploadFile($_FILES['file']['tmp_name'], $_FILES['file']['name']);
    
    echo json_encode($result);
    
});

// #2.2.15 조직도 상태 조회
$app->get('/org-chart/:org_id/status/:db_type', function($org_id, $db_type) use($app) {

    $result;

    // PHP Buildpack에서 amqp TLS로 접속이 안됨.
    // 이번 버전에서는 지원하지 않음
    if ($db_type == 'mysql') {
        $result = array ('status'=>'NO_CHANGES');
    } else if ($db_type == 'cubrid') {
        $result = array ('status'=>'NO_CHANGES');
    } else if ($db_type == 'mongo') {
        $result = array ('status'=>'NO_CHANGES');
    } else {
        $app->status = "500";
    }
    
    echo json_encode($result);
    
});

$app->run();

?>
