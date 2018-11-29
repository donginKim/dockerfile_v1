/**
 * generic-pool 연동
 * mysql 풀 모듈 구현
 */

var generic_pool	= require("generic-pool");
var mysql		= require("mysql");

config = {};
if (process.env.VCAP_SERVICES) {
	// cloud env
	var cloud_env		= JSON.parse(process.env.VCAP_SERVICES);
	var mysql_env		= cloud_env["Mysql-DB"][0]["credentials"];

	config = {
		host:mysql_env.hostname,
		user:mysql_env.username,
		password:mysql_env.password,
		database:mysql_env.name
	};
} else {
	// local env
	config = {
		host:'10.30.40.63',
		user:'cESTBl9QpxGVF5Xa',
		password:'aVu1ynInBnaEeFY0',
		database:'cf_ea68784e_3de6_439d_afc1_d51b4e95627b'
	};
}

var pooling		= generic_pool.Pool({
	name:"mysql",
	create:function(cb){
		var conn = mysql.createConnection(config);
		conn.connect(function(err){
			if( err) console.log("mysql 연결오류");
			else {
			//	console.log("mysql 연결성공");
			}	cb(err, conn);
			// 콜백함수를 통해 풀링에 커넥션 객체를 던짐
		});
	},
	destroy:function(myConn){
		myConn.end(function(err){
			if( err)	console.log("mysql 연결해제오류");
	//		else		console.log("mysql 연결해제성공");
		});
	},
	min:1,
	max:2,
	idleTimeoutMillis:1000*500,
	log:false

});

process.on("exit", function(){
	pooling.drain(function(){
		pooling.destroyAllNow();
	});
});

module.exports = pooling;
