<?php
require  dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require  dirname(__FILE__).'/../vendor/autoload.php';
use OpenCloud\Rackspace;

class GlusterFSView {
    
    private $host;
    private $username;
    private $password;
    private $catalogName;
    private $tenantName;
    private $containerName = 'php-container';
    
    function GlusterFSView() {        

        if (array_key_exists("VCAP_SERVICES", $_ENV)) {

	    $env = json_decode($_ENV["VCAP_SERVICES"], $assoc=true);
            $credential = $env["glusterfs"][0]["credentials"];
            
            $this->host = $credential["auth_url"];
            $this->username = $credential["username"];
            $this->password = $credential["password"];
            $this->catalogName= 'swift';
            $this->tenantName = $credential["tenantname"];

        } else {

          // Local에서 개발을 하기 위한 Connction 정보
            $this->host = 'http://54.199.136.22:5000/v2.0';
            $this->username = 'd34b93e82de4568a';
            $this->password = 'b45cc01d53a4f0e0';
            $this->catalogName= 'swift';
            $this->tenantName = 'op_40dc194b9b0ec4e6';
        }

    }    

    // Org, Group 정보를 조회
    public function uploadFile($file, $fileName) {        

        $client = new OpenCloud\OpenStack($this->host, array(
          "username" => $this->username,
          "password" => $this->password,
          "tenantName" => $this->tenantName,
        ));        
        

        // 인증을 체크한다. 문제가 있으면 401을 Return한다.
        $client->authenticate();
        $service = $client->objectStoreService($this->catalogName, 'RegionOne', 'publicURL');
        
        $container;
        
        // Container를 가져오기
        try {
            
            $container = $service->getContainer($this->containerName);
            
        } catch (Exception $e) {
            
            $container = $service->createContainer($this->containerName);
            
            // 만들어진 Container를 Public 모드로 변경하기
            // PHP-OpenCloud SDK에서 해당 부분을 지원하지 않아서 직접 API를 호출하여 설정함
            $baseUrl = $container->getService()->getEndpoint()->getPublicUrl().'/'.$this->containerName;
            $httpClient = new GuzzleHttp\Client();
            $res = $httpClient->request('POST', $baseUrl, 
                                    ['headers' => ['X-Auth-Token' => $container->getService()->getClient()->getToken(), 'X-Container-Read' => '.r:*']]
                                   );        
            
            // Response Code가 204로 넘어옴. 성공
            
        }

        // 파일 이름 설정
        $fileName = time().'_'.$fileName;
        // 파일 저장
        $result = $container->uploadObject($fileName, fopen($file, 'r+'), array('name'=> $fileName, 'Content-Type' => 'image/jpeg'));

        // 저장된 파일 정보 전달        
        $result = array('thumb_img_path' => $container->getService()->getEndpoint()->getPublicUrl().'/'.$this->containerName.'/'.$fileName);
        
        return $result;
    }

}
?>
