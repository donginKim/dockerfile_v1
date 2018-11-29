/**
 * 디비풀을 이용하여 쿼리를 수행하고 결과를 돌려준다.
 */
var pool = require("./db_pooling");

//Org
exports.insertOrg = function(cb, param){ // param = request.body
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"insert into ORG_TBL (label, `desc`, url, modified) values (?,?,?,CURRENT_TIMESTAMP)",
				[param.label, param.desc, param.url],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.searchOrgAllList = function(cb){ 
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"SELECT * FROM ORG_TBL",
				[],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				});
		}
	});
}

exports.searchOrg = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"SELECT * FROM ORG_TBL WHERE id=? LIMIT 1",
				[param.org_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.updateOrg = function(cb, param){ // param = request
console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var body = param.body;
			var params = param.params;

			conn.query(
				"UPDATE ORG_TBL SET label=?, `desc`=?, url=?, modified=CURRENT_TIMESTAMP WHERE id=?",
				[body.label, body.desc, body.url, params.org_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.deleteOrg = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"DELETE FROM ORG_TBL WHERE id=?",
				[param.org_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

//Group
exports.insertGroup = function(cb, param){ // param = request.body
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				  "insert into GROUP_TBL ( "
				+ "org_id, parent_id, label, "
				+"`desc`, thumb_img_name, thumb_img_path, "
				+"url, modified) "
				+"values (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)",
				[param.org_id, param.parent_id===''? null : param.parent_id, param.label,
				 param.desc, param.thumb_img_name,
				 param.thumb_img_path, param.url],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.searchGroupList = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"SELECT * FROM GROUP_TBL WHERE org_id=?",
				[param.org_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.searchGroup = function(cb, param){ // param = request.params
console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"SELECT * FROM GROUP_TBL WHERE org_id=? AND id=? LIMIT 1",
				[param.org_id, param.groups_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.updateGroup = function(cb, param){ // param = request
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{	
			var body = param.body;
			var params = param.params;
			conn.query(
				  "UPDATE GROUP_TBL SET " 
				+ "org_id=?, parent_id=?, label=?, "
				+ "`desc`=?, thumb_img_name=?, thumb_img_path=?, "
				+ "url=?, modified=CURRENT_TIMESTAMP "
				+ "WHERE org_id=? AND id=?",
				[body.org_id, body.parent_id===''? null : param.parent_id, body.label, 
				 body.desc, body.thumb_img_name, body.thumb_img_path, 
				 body.url,
				 params.org_id, params.groups_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}

exports.deleteGroup = function(cb, param){ // param = request.param
//console.log(param);
	pool.acquire(function(err, conn){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			conn.query(
				"DELETE FROM GROUP_TBL WHERE org_id=? AND id=?",
				[param.org_id, param.groups_id],
				function(err, result){
					if(err) console.log(err);
					else 	console.log(result);
					pool.release(conn);
					cb(err, result);
				}
			);
		}	
	});
}
