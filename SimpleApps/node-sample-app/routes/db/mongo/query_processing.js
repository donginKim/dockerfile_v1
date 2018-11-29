/**
 * 디비풀을 이용하여 쿼리를 수행하고 결과를 돌려준다.
 */
var pool = require("./db_pooling");
var mongodb = require("mongodb");
var ObjectId = require("mongodb").ObjectID;

//Org
exports.insertOrg = function(cb, param){ // param = request.body
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Orgs');
			var doc = {
				label: param.label,
				desc:param.desc,
				url:param.url,
				created: new Date(),
				modified: new Date() 
			};	

			collection.insert(doc, function(err, result) {
				if (err)	console.log(err);
				else	    console.log(result);
				pool.release(db);
				cb(err, result);
			});
		}
	});
}

exports.searchOrgAllList = function(cb){
	pool.acquire(function(err, db) {
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Orgs');
			var query = {};

			collection.find(query).toArray(function(err, result) {
				if (err)	console.log(err);
				else		console.log(result);
				pool.release(db);
				if (result === null) cb(err, []);
				else		     cb(err, JSON.parse(JSON.stringify(result).replace(/"_id":/g, '"id":')));
			});

		}
	});
}

var searchOrg = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Orgs');
			var query = {
				_id: new ObjectId(param.org_id)
			};
			console.log(query);

			collection.findOne(query, function(err, result) {
				if (err)	console.log(err);
				else		console.log(result);
				pool.release(db);
				if (result === null) cb(err, []);
				else		     cb(err, [JSON.parse(JSON.stringify(result).replace(/"_id":/g, '"id":'))]);
			});

		}
	});
}
exports.searchOrg = searchOrg;

exports.deleteOrg = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Orgs');
			var query = {
				_id: new ObjectId(param.org_id)
			};

			collection.remove(query, function(err, result) {
				if (err)
					console.log(err);
				else
				{
					console.log(result);
					deleteGroupByOrgId(function(group_err, group_result){
						if (group_err)	console.log(group_err);
						else		console.log(group_result);
					}, param);
				}
				pool.release(db);
				cb(err, result);
			});
		}
	});
}

exports.updateOrg = function(cb, param){ // param = request
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var body = param.body;
			var params = param.params;

			var collection = db.collection('Orgs');
			var query = {
				_id: new ObjectId(params.org_id)
			};
			var doc = {
				label: body.label,
				desc: body.desc,
				url: body.url,
				modified: new Date()
			};

			collection.update(query, {$set:doc}, function(err, result) {
				if (err)	console.log(err);
				else	    console.log(result);
				pool.release(db);
				cb(err, result);
			});
		}
	});
}

//Group
exports.insertGroup = function(cb, param){ // param = request.body
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Groups');
			var doc = {
				orgId: new ObjectId(param.org_id),
				parentId: param.parent_id ? new ObjectId(param.parent_id) : '',
				label: param.label,
				desc: param.desc,
				thumb_img_name: param.thumb_img_name,
				thumb_img_path: param.thumb_img_path,
				url: param.url,
				created: new Date(),
				 modified: new Date()
			};

			searchOrg(function(org_err, org_result){
				if (org_err || typeof org_result[0] === 'undefined')
					cb(org_err, {result:{ok:0, n:0}});
				else
				{
					if (!param.parent_id)
					{
						collection.insert(doc, function(err, result) {
							if (err)	console.log(err);
							else	    console.log(result);
							pool.release(db);
							cb(err, result);
						});
					}
					else
					{
						searchGroup(function(group_err, group_result){
							if(group_err || typeof group_result[0] === 'undefined')
								cb(group_err, {result: {ok:0, n:0}})
							else
							{
								collection.insert(doc, function(err, result) {
									if (err)	console.log(err);
									else	    console.log(result);
									pool.release(db);
									cb(err, result);
								});
							}
						}, {org_id: param.org_id,
						    groups_id: param.parent_id});
					}
				}
			}, { org_id: param.org_id});
		}
	});
}


exports.searchGroupList = function(cb, param){ // param = request.params
	pool.acquire(function(err, db) {
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Groups');
			var query = {
				orgId: new ObjectId(param.org_id)
			};

			collection.find(query).toArray(function(err, result) {
				if (err)	console.log(err);
				else	    console.log(result);
				pool.release(db);
				if (result === null) cb(err, []);
				//else		     cb(err, JSON.parse(JSON.stringify(result).replace(/"_id":/g, '"id":')));
				else
				{
					var result_str = JSON.stringify(result).replace(/"_id":/g, '"id":');
                                        result_str = result_str.replace(/"parentId":/g, '"parent_id":');
                                        result_str = result_str
							.replace(/"orgId":/g, '"org_id":')
							.replace(/"thumbImgPath":/g,'"thumb_img_path":')
							.replace(/"thumbImgName":/g,'"thumb_img_name":');
                                        cb(err, JSON.parse(result_str));
				}
			});

		}
	});
}

var searchGroup = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Groups');
			var query = {
				_id: new ObjectId(param.groups_id),
				orgId: new ObjectId(param.org_id)
			};
		//	console.log(query);
			collection.findOne(query, function(err, result) {
				if (err)	console.log(err);
				else	    console.log(result);
				pool.release(db);
				if (result === null) cb(err, []);
				//else		     cb(err, [JSON.parse(JSON.stringify(result).replace(/"_id":/g, '"id":'))]);
				else
				{
					var result_str = JSON.stringify(result).replace(/"_id":/g, '"id":');
                                        result_str = result_str.replace(/"parentId":/g, '"parent_id":');
                                        result_str = result_str
							.replace(/"orgId":/g, '"org_id":')
							.replace(/"thumbImgPath":/g,'"thumb_img_path":')
							.replace(/"thumbImgName":/g,'"thumb_img_name":');
                                        cb(err, [JSON.parse(result_str)]);
				}
			});

		}
	});
}
exports.searchGroup = searchGroup;

exports.deleteGroup = function(cb, param){ // param = request.params
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Groups');
			var params = [new ObjectId(param.groups_id)];

			var deleteGroups = function(cnt, group_ids) {
				if (group_ids.length != 0)
				{
					var delete_query = {
						_id: {
							$in: group_ids
						} 
					};
					var find_query = {
						parentId: {
							$in: group_ids
						}
					};

					collection.remove(delete_query, function(err, result) {
						if (err)	console.log(err);
						else	    console.log(result);
					});
					collection.find(find_query, {_id:1}).toArray(function(err, result) {
						if (err)	console.log(err);
						else		console.log(result);
						var params = [];
						for (var i in result) {
							params[i] = new ObjectId(result[i]._id);
						}
						deleteGroups(cnt+result.length, params);
					});
				}
				else
				{
					pool.release(db);
					cb(err, {result:{n:cnt}});
				}
				
			}

			deleteGroups(1, [new ObjectId(param.groups_id)]);
		}
	});
}

var deleteGroupByOrgId = function(cb, param){
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var collection = db.collection('Groups');
			var query = {
				orgId: new ObjectId(param.org_id)
			};

			collection.remove(query, function(err, result) {
				if (err)	console.log(err);
				else	    console.log(result);
				pool.release(db);
				cb(err, result);
			});
		}
	});
}

exports.updateGroup = function(cb, param){ // param = request
//console.log(param);
	pool.acquire(function(err, db){
		if (err) console.log("커넥션 획득 실패 " + err);
		else
		{
			var body = param.body;
			var params = param.params;

			var collection = db.collection('Groups');
			var query = {
				_id: new ObjectId(params.groups_id),
				orgId: new ObjectId(params.org_id),
			};
			var doc = {
				orgId: new ObjectId(body.org_id),
				parentId: body.parent_id || '',
				label: body.label,
				desc: body.desc,
				thumb_img_name: body.thumb_img_name,
				thumb_img_path: body.thumb_img_path,
				url: body.url,
				modified: new Date()
			};

			searchOrg(function(org_err, org_result){
				console.log('Org validate');
				if (org_err || typeof org_result[0] === 'undefined')
					cb(org_err, {result:{ok:0, n:0}});
				else
				{
					if (!body.parent_id)
					{
						collection.update(query, {$set:doc}, function(err, result) {
							if (err)	console.log(err);
							else		console.log(result);
							pool.release(db);
							cb(err, result);
						});
					}
					else
					{
						searchGroup(function(group_err, group_result){
							console.log('group validate');
							if(group_err || typeof group_result[0] === 'undefined')
								cb(group_err, {result:{ok:0, n:0}})
							else
							{
								console.log("dd");
								collection.update(query, {$set:doc}, function(err, result) {
									if (err)	console.log(err);
									else		console.log(result);
									pool.release(db);
									cb(err, result);
								});

							}
						}, { org_id: body.org_id,
						     groups_id: body.parent_id });
					}
				}
			}, { org_id: body.org_id });
		}
	});
}
