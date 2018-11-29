<?php
require  dirname(__FILE__).'/../../lib/vendor/autoload.php';
//require dirname(__FILE__).'/../vendor/autoload.php';

use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Connection\AMQPSSLConnection;
use PhpAmqpLib\Message\AMQPMessage;
use PhpAmqpLib\Channel\AMQPChannel;
use PhpAmqpLib\Connection\AMQPConnection;


class RabbitMQView {
    
    private $host;
    private $port;
    private $username;
    private $password;
    private $vhost;
    private $uri;
    
    function RabbitMQView() {        

        if (array_key_exists("VCAP_SERVICES", $_ENV)) {

            $env = json_decode($_ENV["VCAP_SERVICES"], $assoc=true);
            $credentials = $env["p-rabbitmq"][0]["credentials"]["protocols"]["amqp+ssl"]; 
            $this->host = $credentials["host"];
            $this->port = $credentials["port"];
            $this->username = $credentials["username"];
            $this->password = $credentials["password"];
            $this->vhost = $credentials["vhost"];
            $this->uri = ["uri"];

        } else {

            $this->host = '127.0.0.1';
            $this->port = 51011;

            $this->username = 'e9032f3d-d43f-4286-8398-63da2860dfd6';
            $this->password = 'f3ibaebbeik9qv8qcdt8udr3pk';
            $this->vhost = '6ffb4d8a-8748-4f00-a338-80e6eadee822';        
            $this->uri = "amqps://e9032f3d-d43f-4286-8398-63da2860dfd6:f3ibaebbeik9qv8qcdt8udr3pk@10.30.40.82:5671/6ffb4d8a-8748-4f00-a338-80e6eadee822";      

        }
        
    }
    
    // Queue에 등록
    // RabbitMQ서비스에서 amqps로 Connection이 되지 않음
    public function insertQueue($org_id, $db_type, $message) {        
        
        $conn = new AMQPSSLConnection($this->host, $this->port, $this->username, $this->password, $this->vhost);
        $channel = $conn->channel();        

        $queue = $org_id."_".$db_type;
        $channel->queue_declare($queue, false, false, false, false);

        $msg = new AMQPMessage($message);
        $channel->basic_publish($msg, '', $queue);

        $channel->close();
        $conn->close();        
        
        $result = array ();
        return $result;
    }

    // Queue 검색
    public function getQueue($org_id, $db_type) {

        $conn = new AMQPSSLConnection($this->host, $this->port, $this->username, $this->password, $this->vhost);
        $channel = $conn->channel();        

        $queue = $org_id."_".$db_type;
        $channel->queue_declare($queue, false, false, false, false);
        $result = array();

        $callback = function($msg) {

            $result = array('status'=>$msg->body);
            
            $channel->close();
            $conn->close();        
        };

        $channel->basic_consume($queue, '', false, true, false, false, $callback);

        while(count($channel->callbacks)) {
            $channel->wait();
        }
        
        return $result;
    }

}
?>
