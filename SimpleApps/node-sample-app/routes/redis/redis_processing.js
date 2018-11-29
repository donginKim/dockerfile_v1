var redis = require("./redis");
var uuid = require('uuid');

exports.set = function(cb, param){ // req.body.id
	redis.open(function(client){
		var key = uuid.v4();
		client.set(key, param, function(err, result){
			if (err)	console.log("err: " + err);
			else		console.log("result: " + result);
			cb(err, {"key": key, "result":result});
			redis.close();
		});
	});
}
exports.del = function(cb, key){
	redis.open(function(client){
		client.del(key, function(err, result){
			if (err)	console.log("err: " + err);
		        else		console.log("result: " + result); 
			cb(err, {"key": key, "result":result});
			redis.close();
		});
	});
}
