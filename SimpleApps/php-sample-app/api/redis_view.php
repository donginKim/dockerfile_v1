<?php
require  dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require  dirname(__FILE__).'/../vendor/autoload.php';

Predis\Autoloader::register();


class RedisView {
    
    private $host;
    private $port;
    private $password;
    
    function RedisView() {        

        if (array_key_exists("VCAP_SERVICES", $_ENV)) {

            $env = json_decode($_ENV["VCAP_SERVICES"], $assoc=true);
            $credential = $env["redis-sb"][0]["credentials"];
            
            $this->host = $credential["host"];
            $this->port = $credential["port"];
            $this->password = $credential["password"];

        } else {
        
            $this->host = '127.0.0.1';
            $this->port = 51012;
//            $this->host = '10.20.40.71';
//            $this->port = 34838;
            $this->password = 'c239b721-d986-4ee3-8816-b5f5fa9f3ffb';
            
        }
    }
    
    // Key에 Username 저장하기
    public function insertKey($key, $username) {        
        
        $redis = new Predis\Client(
                    array(
                        "scheme" => "tcp",
                        "host" => $this->host,
                        "port" => $this->port,
                        "password" => $this->password
                ));
            
        $redis->set($key, $username);
        
        $result = $redis->get($key);

        return $result;
    }

    // Key의 값 가져오기
    public function getKey($key) {
        
        $redis = new Predis\Client(
                    array(
                        "scheme" => "tcp",
                        "host" => $this->host,
                        "port" => $this->port,
                        "password" => $this->password
                ));
            
        $result = $redis->get($key);        
        
        return $result;
    }

    // Key 삭제하기
    public function deleteKey($key) {
        
        $redis = new Predis\Client(
                    array(
                        "scheme" => "tcp",
                        "host" => $this->host,
                        "port" => $this->port,
                        "password" => $this->password
                ));
            
        $value = $redis->del($key);        
        
        $result = array (
            "RESULT" => $value
        );
        
        return $result;
    }
}

?>