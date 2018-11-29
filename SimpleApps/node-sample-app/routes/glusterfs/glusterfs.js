var pkgcloud = require('pkgcloud');
var http = require('http');
var url = require('url');

var credentials = {};
var container_name = 'node_container';
if (process.env.VCAP_SERVICES) { 
	// cloud env. 
	var services = JSON.parse(process.env.VCAP_SERVICES); 
	var glusterfsConfig = services["glusterfs"]; 
 
	if (glusterfsConfig) { 
		var config = glusterfsConfig[0]; 
		credentials = {
			provider: 'openstack', // 
		        username: config.credentials.username,
		        password: config.credentials.password,
			authUrl:  config.credentials.auth_url.substring(0, config.credentials.auth_url.lastIndexOf('/')),
			region: 'RegionOne' //
		};
	}
} else {
	// local env.
		credentials = {
			provider: 'openstack',
		        username: 'cf13d551d997458e', 
		        password: 'b45cc01d53a4f0e0',
			authUrl:  'http://54.199.136.22:5000/',
			region: 'RegionOne'
		};
}

var client = pkgcloud.storage.createClient(credentials);

// delete container for test
/*
client.destroyContainer(container_name, function(err, result){
	if (err) console.log(err);
	else console.log(result);
});
*/

// check container
client.getContainer(container_name, function(err, container){
        if (err)
        {
		// if container not exist
                if (err.statusCode === 404)
                {
			// create container
                        client.createContainer({name:container_name}, function(create_err, create_container){
                                if (create_err) console.log(err);
				else
				{	 
					// if container created successfully, setting a readable member(X-Contaner-Read: .r:*)
					// 컨테이너가 성공적으로 생성되었다면 컨테이너를 누구나 읽을 수 있게 설정한다.(X-Contaner-Read: .r:*)
					// There is a bug in the code(pkgcloud). so i used api call.
					// pkgcloud 모듈에서 metadata를 넣을 경우 prefix가 붙는 로직때문에 제대로 위의 값이 입력이 안되므로 api를 통해서 설정.
					var serviceUrl = url.parse(create_container.client._serviceUrl);
					var option = {
						host: serviceUrl.hostname,
						port: serviceUrl.port,
						path: serviceUrl.path+'/'+container_name,
						method: 'POST',
						headers: {
							'X-Auth-Token': create_container.client._identity.token.id,
							'X-Container-Read': '.r:*' // ACL form
						}
					};
					var req = http.request(option, function(res){
					});
					req.end();
				}

                        });
                }
                else    console.log(err);
        }
});

module.exports = client;