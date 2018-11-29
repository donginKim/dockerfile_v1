# -*- coding: utf-8 -*-
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from appsample import rabbitmq_views
import json
from bson import ObjectId,json_util,datetime
from collections import OrderedDict
from pymongo import MongoClient
from django.conf import settings

def make_connection():
    if settings.MONGO_CRED:
        credentials = settings.MONGO_CRED
        mongo_hosts = credentials['hosts'][0].split(':')
        connection = MongoClient(mongo_hosts[0], int(mongo_hosts[1]))
        return connection
    else:
        return JSONResponse({'errors':'MongoDB service not bound'})

# 데이터 베이스 인증
def db_auth(connection):
    connection = connection
    db = connection[credentials['name']]
    db.authenticate(credentials['username'],credentials['password'])
    return db

class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

class CustomEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, ObjectId):
            return str(obj)
        elif isinstance(obj, datetime.datetime):
            return obj.date().isoformat()
        return json.JSONEncoder.default(self, obj)

@csrf_exempt
def org_chart(request, org_id):

    connection = make_connection()
    db =db_auth(connection)

    if type(db)==JSONResponse:
        return db

    #2.2.1. 조직 및 그룹(하위조직) 리스트 요청 
    if request.method == 'GET':
        cursor = db.Orgs.find_one({"_id":ObjectId(org_id)})
        encoded = CustomEncoder().encode(cursor)
        org = json.loads(encoded, object_hook=json_util.object_hook)
        if org:
            org['id'] = org.pop('_id')
        cursor = db.Groups.find({"orgId":ObjectId(org_id)})
        encoded = CustomEncoder().encode(list(cursor))
        groups = json.loads(encoded, object_hook=json_util.object_hook)

        for group in groups:
            group['id'] = group.pop('_id')
            group['org_id'] = group.pop('orgId')
            group['parent_id'] = group.pop('parentId')
            group['thumb_img_path'] = group.pop('thumbImgPath')
            group['thumb_img_name'] = group.pop('thumbImgName')

        connection.close()
        return JSONResponse(OrderedDict(org=org,groups=groups), status=200)

@csrf_exempt
def org_list(request):
    connection = make_connection()
    db =db_auth(connection)

    if type(db)==JSONResponse:
        return db

    #2.2.2. 조직 리스트 요청
    if request.method == 'GET':
        cursor = db.Orgs.find()
        encoded = CustomEncoder().encode(list(cursor))
        orgs = json.loads(encoded, object_hook=json_util.object_hook)

        for org in orgs:
            org['id'] = org.pop('_id')
       
        connection.close()
        return JSONResponse(dict(orgs=orgs), status=200)

    #2.2.4. 조직 등록
    elif request.method == 'POST':
        data = id_type_change_str_to_object_id(request)
        insert_query = make_query('POST',data)
        org = db.Orgs.insert(insert_query)
        
        connection.close()
        return JSONResponse({"org":str(org)},status=201)

@csrf_exempt
def org_list_detail(request, org_id):
    connection = make_connection()
    db =db_auth(connection)

    if type(db)==JSONResponse:
        return db
                
    #2.2.3. 조직 정보 요청
    if request.method == 'GET': 
        cursor = db.Orgs.find_one({"_id":ObjectId(org_id)})
        encoded = CustomEncoder().encode(cursor)

        org = json.loads(encoded, object_hook=json_util.object_hook)
        if org:
            org['id'] = org.pop('_id')

        connection.close()
        return JSONResponse(dict(org=org), status=200)

    elif request.method == 'PUT':
        data = id_type_change_str_to_object_id(request)
        update_query = make_query('PUT',data)
        db.Orgs.update({"_id":ObjectId(org_id)},{"$set":update_query},upsert=False, multi=False)
        rabbitmq_views.sendOrg(org_id,'mongo','ORG_UPDATED')
        
        connection.close() 
        return JSONResponse({},status=201)

    #2.2.6. 조직 삭제
    elif request.method == 'DELETE':
        db.Orgs.remove({"_id":ObjectId(org_id)})
        db.Groups.remove({"orgId":ObjectId(org_id)})
        rabbitmq_views.sendOrg(org_id,'mongo','ORG_DELETED')
        
        connection.close()
        return JSONResponse({},status=200)

@csrf_exempt
def group_list(request, org_id):
    connection = make_connection()
    db =db_auth(connection)

    if type(db)==JSONResponse:
        return db    
        
    #2.2.7. 그룹(하위 조직) 리스트 요청
    if request.method == 'GET':
        cursor = db.Groups.find({"orgId":ObjectId(org_id)})
        encoded = CustomEncoder().encode(list(cursor))
        groups = json.loads(encoded, object_hook=json_util.object_hook)
        for group in groups:
            group['id'] = group.pop('_id')
            group['org_id'] = group.pop('orgId')
            group['parent_id'] = group.pop('parentId')
            group['thumb_img_path'] = group.pop('thumbImgPath')
            group['thumb_img_name'] = group.pop('thumbImgName')
        
        connection.close()
        return JSONResponse(dict(groups=groups), status=200)

    #2.2.9 그룹(하위 조직) 등록
    elif request.method == 'POST':
        data = id_type_change_str_to_object_id(request)
        insert_query = make_query('POST',data)
        group = db.Groups.insert(insert_query)

        rabbitmq_views.sendOrg(org_id,'mongo','GROUP_ADDED')

        connection.close()
        return JSONResponse({"group":str(group)},status=201)

@csrf_exempt
def group_list_detail(request, org_id, group_id):
    connection = make_connection()
    db =db_auth(connection)

    if type(db)==JSONResponse:
        return db

    #2.2.8 그룹(하위 조직) 정보 요청
    if request.method == 'GET':
        cursor = db.Groups.find_one({"_id":ObjectId(group_id)})
        encoded = CustomEncoder().encode(cursor)
        group = json.loads(encoded, object_hook=json_util.object_hook)
        if group:
            group['id'] = group.pop('_id')

        connection.close()
        return JSONResponse(dict(group=group), status=200)

    # 2.2.10. 그룹(하위 조직) 수정
    elif request.method == 'PUT':
        data = id_type_change_str_to_object_id(request)
        update_query = make_query('PUT',data)
        db.Groups.update({"_id":ObjectId(group_id)},{"$set":update_query},upsert=False, multi=False)
        rabbitmq_views.sendOrg(org_id,'mongo','GROUP_UPDATED')

        connection.close()
        return JSONResponse({},status=201)

    #2.2.11. 그룹(하위조직) 삭제
    elif request.method == 'DELETE':
        cursor = db.Groups.find_one({"_id":ObjectId(group_id)})
        encoded = CustomEncoder().encode(cursor)
        group = json.loads(encoded, object_hook=json_util.object_hook)
        parricide(group,org_id,db)
        db.Groups.remove({"_id":ObjectId(group_id)})
        rabbitmq_views.sendOrg(org_id,'mongo','GROUP_DELETED')

        connection.close()
        return JSONResponse({},status=200)


def make_query(method, data):
    now = datetime.datetime.now()
    
    if 'org_id' in data:
        data['orgId'] = data.pop('org_id')

    if 'parent_id' in data:
        data['parentId'] = data.pop('parent_id')
    
    if 'thumb_img_path' in data:
        data['thumbImgPath'] = data.pop('thumb_img_path')
    
    if 'thumb_img_name' in data:
        data['thumbImgName'] = data.pop('thumb_img_name')

    if method == 'POST':
        data.update(created=now, modified='')
    
    elif method =='PUT':
        data.update(modified=now)
    return data

def id_type_change_str_to_object_id(request):
    data = JSONParser().parse(request)
    if 'org_id' in data:
        #그룹의 org_id를 ObjectId로 변환하고 parent_id는 빈값이 아닐 경우에만 ObjectId로 변환한다.
        data['org_id'] = ObjectId(data['org_id'])
        if not data['parent_id'] =="":
            data['parent_id'] = ObjectId(data['parent_id'])
    return data

#자식 삭제
def parricide(parent,org_id,db):
    parent_id = dict(parent)["_id"]
    cursor = db.Groups.find({"orgId":ObjectId(org_id),"parentId":ObjectId(parent_id)})
    encoded = CustomEncoder().encode(list(cursor))
    children = json.loads(encoded, object_hook=json_util.object_hook)
    if not children:
        db.Groups.remove({"_id":ObjectId(parent_id)})
    else:
        for child in children:
            db.Groups.remove({"_id":ObjectId(parent_id)})
            parricide(child,org_id,db)