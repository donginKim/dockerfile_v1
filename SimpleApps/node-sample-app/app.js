var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');

var app = express();

// URL 매핑 후 수행할 js 파일들
var org_chart_mysql = require('./routes/rest/org_chart/mysql')
  , org_chart_mongo = require('./routes/rest/org_chart/mongo')
  , org_chart_cubrid = require('./routes/rest/org_chart/cubrid')
  , orgs_mysql = require('./routes/rest/orgs/mysql')
  , orgs_mongo = require('./routes/rest/orgs/mongo')
  , orgs_cubrid = require('./routes/rest/orgs/cubrid')
  , groups_mysql = require('./routes/rest/groups/mysql')
  , groups_mongo = require('./routes/rest/groups/mongo')
  , groups_cubrid = require('./routes/rest/groups/cubrid')
  , login = require('./routes/rest/login/login')
  , image = require('./routes/rest/image/image')
  , page = require('./routes/page/page_processing');

// view engine setup
// 뷰 엔진 설정
app.set('views', path.join(__dirname, 'views/'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
// 정적파일 위치 설정
app.use(express.static(path.join(__dirname, 'public')));

/*
* URL 매핑 설정
*/
//app.use(   '/', routes);
//app.use(   '/users', users);

// page
app.get(   '/', page.login);
app.get(   '/login', page.login);
app.get(   '/manage', page.manage);
app.get(   '/main/:id', page.main);

// org-chart
app.get(   '/org-chart/:org_id/mysql', org_chart_mysql.index);
app.get(   '/org-chart/:org_id/cubrid', org_chart_cubrid.index);
app.get(   '/org-chart/:org_id/mongo', org_chart_mongo.index);
app.get(   '/org-chart/:org_id/status/mysql', org_chart_mysql.status);
app.get(   '/org-chart/:org_id/status/cubrid', org_chart_cubrid.status);
app.get(   '/org-chart/:org_id/status/mongo', org_chart_mongo.status);

//orgs
//  mysql
app.get(   '/orgs/mysql', orgs_mysql.index);
app.post(  '/orgs/mysql', orgs_mysql.create);
app.get(   '/orgs/:org_id/mysql', orgs_mysql.show);
app.put(   '/orgs/:org_id/mysql', orgs_mysql.update);
app.delete('/orgs/:org_id/mysql', orgs_mysql.destroy);
//  mongo
app.get(   '/orgs/mongo', orgs_mongo.index);
app.post(  '/orgs/mongo', orgs_mongo.create);
app.get(   '/orgs/:org_id/mongo', orgs_mongo.show);
app.put(   '/orgs/:org_id/mongo', orgs_mongo.update);
app.delete('/orgs/:org_id/mongo', orgs_mongo.destroy);
//  cubrid
app.get(   '/orgs/cubrid', orgs_cubrid.index);
app.post(  '/orgs/cubrid', orgs_cubrid.create);
app.get(   '/orgs/:org_id/cubrid', orgs_cubrid.show);
app.put(   '/orgs/:org_id/cubrid', orgs_cubrid.update);
app.delete('/orgs/:org_id/cubrid', orgs_cubrid.destroy);

//groups
//  mysql
app.get(   '/orgs/:org_id/groups/mysql', groups_mysql.index);
app.post(  '/orgs/:org_id/groups/mysql', groups_mysql.create);
app.get(   '/orgs/:org_id/groups/:groups_id/mysql', groups_mysql.show);
app.put(   '/orgs/:org_id/groups/:groups_id/mysql', groups_mysql.update);
app.delete('/orgs/:org_id/groups/:groups_id/mysql', groups_mysql.destroy);
//  mongo
app.get(   '/orgs/:org_id/groups/mongo', groups_mongo.index);
app.post(  '/orgs/:org_id/groups/mongo', groups_mongo.create);
app.get(   '/orgs/:org_id/groups/:groups_id/mongo', groups_mongo.show);
app.put(   '/orgs/:org_id/groups/:groups_id/mongo', groups_mongo.update);
app.delete('/orgs/:org_id/groups/:groups_id/mongo', groups_mongo.destroy);
//  cubrid
app.get(   '/orgs/:org_id/groups/cubrid', groups_cubrid.index);
app.post(  '/orgs/:org_id/groups/cubrid', groups_cubrid.create);
app.get(   '/orgs/:org_id/groups/:groups_id/cubrid', groups_cubrid.show);
app.put(   '/orgs/:org_id/groups/:groups_id/cubrid', groups_cubrid.update);
app.delete('/orgs/:org_id/groups/:groups_id/cubrid', groups_cubrid.destroy);

//login/logout
app.post(  '/manage/login', login.login);
app.post(  '/manage/logout', login.logout);

//image
app.post(  '/upload', image.upload);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
