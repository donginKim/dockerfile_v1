var rabbitMQ = require('./rabbitMQ');
var ex_name = 'node-app';
exports.publish = function(q_name, msg){
	rabbitMQ.open(function(conn){
		conn.exchange(ex_name,{}, function(ex){
			conn.queue(q_name, {autoDelete:true},function(q){
				q.bind(ex_name, '');
				q.on('queueBindOk', function() { 
					ex.publish('', msg);
				});
				setTimeout(function () {
					// wait one second to send the message, then quit
					q.unbind(ex_name, '');
					conn.disconnect();
				}, 1000);
			});
		});
	});
}

exports.subscribe = function(q_name, cb){
//	console.log('sub');
	var msg = '';
	conn = rabbitMQ.open(function(c){
//		console.log('open');
		c.exchange(ex_name,{}, function(ex){
//			console.log('exchage');
			que = c.queue(q_name, {autoDelete:true},function(q){
//				console.log('queue');
				q.bind(ex_name, '');
				q.on('queueBindOk', function() { 
//					console.log('bindok');
					q.subscribe(function(m){
//						console.log('q.sub');
						msg = m.data.toString();
						m.acknowledge();
					});
					q.unbind(ex_name, '');
					c.disconnect();
				});
			});
		});
	});
	setTimeout(function () {
	// wait one second to subscribe the message, then quit
//		console.log('msg: '+ msg);
		cb(msg);
	}, 1000);
}
