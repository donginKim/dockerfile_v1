var amqp = require('amqp');

var url = '';
if (process.env.VCAP_SERVICES) {
	// cloud env.
        var services = JSON.parse(process.env.VCAP_SERVICES);
        var rabbitMQConfig = services["p-rabbitmq"];

        if (rabbitMQConfig) {
                var node = rabbitMQConfig[0];
		url = node.credentials.uri;
        }

} else {
	// local env.
	url = 'amqps://14b1ab93-4cdb-46af-8cdd-8d8073bbe282:cl71e9ihgu6gvhj1eiqj9uh4um@10.30.40.82:5671/6ffb4d8a-8748-4f00-a338-80e6eadee822';
}

exports.open = function(cb){
	// create connection.
	var conn = amqp.createConnection({url: url});
	
	// it must be cb(callback) after the 'ready' event.
	conn.on('ready', function(){
		cb(conn);
	});
}

// not used. 
/*
exports.close = function(){
	conn.disconnect();
}
*/
