from django.conf.urls import url
from appsample import mysql_views
from appsample import rabbitmq_views
from appsample import cubrid_views
from appsample import mongo_views
from appsample import redis_views
from appsample.gluster_views import UploadView
from appsample.template_views import LoginView, MainView, ManageView

urlpatterns = [
    #mysql
    url(r'^org-chart/(?P<org_id>[0-9]+)/mysql/?$', mysql_views.org_chart),
    url(r'^orgs/mysql/?$', mysql_views.org_list),
    url(r'^orgs/(?P<org_id>[0-9]+)/mysql/?$', mysql_views.org_list_detail),
    url(r'^orgs/(?P<org_id>[0-9]+)/groups/mysql/?$', mysql_views.group_list),
    url(r'^orgs/(?P<org_id>[0-9]+)/groups/(?P<group_id>[0-9]+)/mysql/?$', mysql_views.group_list_detail),
    
    #cubrid
    url(r'^org-chart/(?P<org_id>[0-9]+)/cubrid/?$', cubrid_views.org_chart),
    url(r'^orgs/cubrid/?$', cubrid_views.org_list),
    url(r'^orgs/(?P<org_id>[0-9]+)/cubrid/?$', cubrid_views.org_list_detail),
    url(r'^orgs/(?P<org_id>[0-9]+)/groups/cubrid/?$', cubrid_views.group_list),
    url(r'^orgs/(?P<org_id>[0-9]+)/groups/(?P<group_id>[0-9]+)/cubrid/?$', cubrid_views.group_list_detail),
    
    #mongoDB
    url(r'^org-chart/(?P<org_id>[0-9a-zA-Z]+)/mongo/?$', mongo_views.org_chart),
    url(r'^orgs/mongo/?$', mongo_views.org_list),
    url(r'^orgs/(?P<org_id>[0-9a-zA-Z]+)/mongo/?$', mongo_views.org_list_detail),
    url(r'^orgs/(?P<org_id>[0-9a-zA-Z]+)/groups/mongo/?$', mongo_views.group_list),
    url(r'^orgs/(?P<org_id>[0-9a-zA-Z]+)/groups/(?P<group_id>[0-9a-zA-Z]+)/mongo/?$', mongo_views.group_list_detail),
    
    #rabbitmq
    #url(r'^org-chart/(?P<org_id>[0-9a-zA-Z]+)/status/(?P<db_type>[mysql,cubrid,mongo]+)/(?P<msg>[a-zA-Z]+)/?$', rabbitmq_views.sendOrg),
    url(r'^org-chart/(?P<org_id>[0-9a-zA-Z]+)/status/(?P<db_type>[mysql,cubrid,mongo]+)/?$', rabbitmq_views.receiveOrg),
    
    #redis
    url(r'^manage/login/?$', redis_views.login),
    url(r'^manage/logout/?$', redis_views.logout),

    #gluster
    url(r'^upload/?$', UploadView.as_view()),
    
    #template_views
    url(r'^$', LoginView.as_view()),
    url(r'^main/[0-9a-zA-Z]+?$', MainView.as_view()),
    url(r'^manage/?$', ManageView.as_view()),
]