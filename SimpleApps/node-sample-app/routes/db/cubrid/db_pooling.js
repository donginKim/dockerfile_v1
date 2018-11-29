/**
 * generic-pool 연동
 * cubrid 풀 모듈 구현
 */

var generic_pool	= require("generic-pool");
var cubrid		= require("node-cubrid");

var 	database
	, port
	, hostname
	, username
	, password;
if (process.env.VCAP_SERVICES) {
	// cloud env
	var cloud_env		= JSON.parse(process.env.VCAP_SERVICES);
	var cubrid_env		= cloud_env["CubridDB"][0]["credentials"];

 	database	= cubrid_env.name
	port		= cubrid_env.port
	hostname	= cubrid_env.hostname
	username	= cubrid_env.username
	password	= cubrid_env.password;
} else {
	// local env
 	database	= 'fccf1d7869ff72ce'
	port		= '' 
	hostname	= '10.30.60.23' 
	username	= 'b2f6b4af1e7bd7d8'
	password	= '45f179c648ee60a5';
}
var pooling		= generic_pool.Pool({
	name:"cubrid",
	create:function(cb){
//		console.log("cubrid_env.uri:" + cubrid_env.uri);
		var conn = cubrid.createCUBRIDConnection(hostname, port, username, password, database);
		conn.connect(function(err){
			if( err) console.log("cubrid 연결오류");
			else{
//				console.log("cubrid 연결성공");
				cb(err, conn);
			}
			// 콜백함수를 통해 풀링에 커넥션 객체를 던짐
		});
	},
	destroy:function(myConn){
		myConn.end(function(err){
			if( err)	console.log("cubrid 연결해제오류");
//			else		console.log("cubrid 연결해제성공");
		});
	},
	min:1,
	max:2,
	idleTimeoutMillis:1000*50,
	log:false,
	
});

process.on("exit", function(){
	pooling.drain(function(){
		pooling.destroyAllNow();
	});
});

module.exports = pooling;
