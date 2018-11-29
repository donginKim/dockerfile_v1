var redis = require("redis");

var options = {};
if (process.env.VCAP_SERVICES) {
	// cloud env.
	var services = JSON.parse(process.env.VCAP_SERVICES);
	var redisConfig = services["redis-sb"];

	if (redisConfig) {
  		var node = redisConfig[0];
  		options = {
			host: node.credentials.host,
			port: node.credentials.port,
			pass: node.credentials.password,
 		};
	}

} else {
	// local env.
  	options = {
		host: '10.30.40.71',
		port: '34838',
		pass: 'c239b721-d986-4ee3-8816-b5f5fa9f3ffb',
 	};
}

var client = null;
exports.open = function(cb) {
//	console.log(JSON.stringify(options));
	client = redis.createClient(options);
	// get auth.
	client.auth(options.pass);

	cb(client);
}
exports.close = function(){
	client.end();
}
