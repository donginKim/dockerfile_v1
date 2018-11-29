var client = require("./glusterfs");
var container_name = 'node_container';
var fs = require('fs');

exports.uploadImg = function(image, cb){
	var readStream = fs.createReadStream(image.path);
	var writeStream = client.upload({
		container: container_name,
		contentType: image.type,
		remote: new Date().getTime() + '_' + image.name,
	});

	writeStream.on('error', function(err) {
		cb(err, '');
	});

	writeStream.on('success', function(file) {
//		console.log(file);
		cb('', file);
	});

	readStream.pipe(writeStream);
}
