# -*- coding: utf-8 -*-
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from appsample import rabbitmq_views
from django.db import connections
from collections import OrderedDict

def make_connection():
    db_type = 'default'
    cursor = connections[db_type].cursor()
    return cursor

class JSONResponse(HttpResponse):

    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@csrf_exempt
def org_chart(request, org_id):
    cursor = make_connection()

    #2.2.1. 조직 및 그룹(하위조직) 리스트 요청 

    if request.method == 'GET':

        cursor.execute('SELECT * FROM ORG_TBL WHERE id = %s',[org_id])
        org = dictfetchall(cursor)
        cursor.execute('SELECT * FROM GROUP_TBL WHERE org_id = %s',[org_id])
        groups = dictfetchall(cursor)
        
        type_changed_org = change_to_valid_str(org)
        type_changed_groups = change_to_valid_str(groups)

        try:
            response_dict = OrderedDict([("org",type_changed_org[0])])
        except IndexError as e:
            response_dict = OrderedDict({"org":{}})
        
        response_dict.update({"groups":type_changed_groups})
        
        cursor.close()

        return JSONResponse(response_dict, status=200)


@csrf_exempt
def org_list(request):
    cursor = make_connection()
    
    #2.2.2. 조직 리스트 요청
    if request.method == 'GET':
        cursor.execute("SELECT * FROM ORG_TBL")


        #DB에 저장된 값이 없을 경우 에러처리
        dict_list = dictfetchall(cursor)
        type_changed = change_to_valid_str(dict_list)

        cursor.close()

        return JSONResponse({"orgs":type_changed}, status=200)

    #2.2.4. 조직 등록
    elif request.method == 'POST':
        data = change_id_type_to_int(request)
        sql = make_query('POST', 'ORG_TBL', data=data)
        cursor.execute(sql)

        cursor.close()
        return JSONResponse({}, status=201)


@csrf_exempt
def org_list_detail(request, org_id):
    cursor = make_connection()

    #2.2.3. 조직 정보 요청
    if request.method == 'GET':
        cursor.execute('SELECT * FROM ORG_TBL WHERE id = %s',[org_id])
        dict_list = dictfetchall(cursor)
        type_changed = change_to_valid_str(dict_list)
        
        cursor.close()
        
        try:
            return JSONResponse({"org":type_changed[0]}, status=200)
        except IndexError as e:
            return JSONResponse({"org":{}}, status=400)

    #2.2.5. 조직 수정
    elif request.method == 'PUT':
        data = change_id_type_to_int(request)
        sql = make_query('PUT','ORG_TBL',data=data, org_id=org_id)
        cursor.execute(sql)
        cursor.close()        
        rabbitmq_views.sendOrg(org_id,'mysql','ORG_UPDATED')
        return JSONResponse({}, status=201)

    #2.2.6. 조직 삭제
    elif request.method == 'DELETE':
        cursor.execute('DELETE FROM ORG_TBL WHERE id = %s', [org_id])
        cursor.execute('DELETE FROM GROUP_TBL WHERE org_id = %s', [org_id])
        cursor.close()
        rabbitmq_views.sendOrg(org_id,'mysql','ORG_DELETED')
        return JSONResponse({},status=200)
    

@csrf_exempt
def group_list(request, org_id):
    cursor = make_connection()
    
    #2.2.7. 그룹(하위 조직) 리스트 요청
    if request.method == 'GET':
        cursor.execute('SELECT * FROM GROUP_TBL WHERE org_id = %s', [org_id])
        dict_list = dictfetchall(cursor)
        type_changed = change_to_valid_str(dict_list)

        cursor.close()        
        return JSONResponse({"groups":type_changed}, status=200)
    
    #2.2.9 그룹(하위 조직) 등록
    elif request.method == 'POST':
        data = change_id_type_to_int(request)
        sql = make_query('POST','GROUP_TBL',data=data)
        cursor.execute(sql)
        cursor.close()        
        rabbitmq_views.sendOrg(org_id,'mysql','GROUP_ADDED')
        return JSONResponse({}, status=201)

@csrf_exempt
def group_list_detail(request, org_id, group_id):
    cursor = make_connection()

    #2.2.8 그룹(하위 조직) 정보 요청
    if request.method == 'GET':
        cursor.execute('SELECT * FROM GROUP_TBL WHERE id = %s', [group_id])
        dict_list = dictfetchall(cursor)
        type_changed = change_to_valid_str(dict_list)
        cursor.close()
        try:
            return JSONResponse({"group":type_changed[0]}, status=201)
        except IndexError as e:
            return JSONResponse({"group":{}}, status=400)

    # 2.2.10. 그룹(하위 조직) 수정
    elif request.method == 'PUT':
        data = change_id_type_to_int(request)
        sql = make_query('PUT','GROUP_TBL', data=data, group_id=group_id)
        cursor.execute(sql)
        cursor.close()        
        rabbitmq_views.sendOrg(org_id,'mysql','GROUP_UPDATED')
        return JSONResponse({}, status=201)

    #2.2.11. 그룹(하위조직) 삭제
    elif request.method == 'DELETE':
        cursor.execute('DELETE FROM GROUP_TBL WHERE id = %s', [group_id])
        rabbitmq_views.sendOrg(org_id,'mysql','GROUP_DELETED')
        return JSONResponse({},status=200)

    cursor.close()

def make_query(method, table_name, **kwargs):
    parent_null=False
    if 'data' in kwargs:
        data = kwargs['data']
        if 'parent_id' in data:
            if data['parent_id'] == 0:
                del data['parent_id']
                parent_null = True
    
    if 'org_id' in kwargs:
        id = kwargs['org_id']
    elif 'group_id' in kwargs:
        id = kwargs['group_id']

    if method == 'POST':
        keys = data.keys()
        kString = ",".join(keys)
        sql_columns = kString.replace("desc","`desc`") #컬럼명 중 desc는 mysql 예약어이기 때문에 `desc`로 변경한다.
        values = data.values()
        sql_values = ''
        for v in values:

            if isinstance(v, int):
                sql_values += "".join(["%s," %v])
            else:
                sql_values += "".join(["'%s'," %v])
        
        sql_values = sql_values[:-1]

        if parent_null:
            sql_columns=sql_columns+",parent_id"
            sql_values=sql_values+",null"

        sql = "INSERT INTO %s (%s) VALUES (%s);" %(table_name, sql_columns, sql_values)
        return sql

    elif method =='PUT':
        update_items = ''
        for item in data.items():
            update_items += "%s = '%s' ," %(item[0].replace("desc","`desc`"), item[1])

        update_items = update_items + 'modified = CURRENT_TIMESTAMP'
        
        if parent_null:
            update_items = update_items+',parent_id = null'

        sql = "UPDATE %s SET %s WHERE id = %s;" %(table_name, update_items, id)
        return sql

def dictfetchall(cursor):
    columns = [col[0] for col in cursor.description]
    return [
        OrderedDict(zip(columns, row))
        for row in cursor.fetchall()
    ]

def change_to_valid_str(dict_list):
    for dictionary in dict_list:
        dictionary['id'] = str(dictionary['id'])
        if 'org_id' in dictionary:
            dictionary['org_id'] = str(dictionary['org_id'])
            if dictionary['parent_id'] == None or dictionary['parent_id'] == 0:
                dictionary['parent_id'] = ''
            else:
                dictionary['parent_id'] = str(dictionary['parent_id'])
        
        if not (dictionary['created'] == None or dictionary['created'] == ''):
            dictionary['created'] = dictionary['created'].date().isoformat()
        if not (dictionary['modified'] == None or dictionary['modified'] == ''):
            dictionary['modified'] = dictionary['modified'].date().isoformat()
    
    return dict_list

def change_id_type_to_int(request):
    data = JSONParser().parse(request)

    if 'id' in data:
        data['id'] = int(data['id'])

    if 'org_id' in data:
        data['org_id'] = int(data['org_id'])
        if data['parent_id'] == '' or data['parent_id'] == None:
            data['parent_id'] = 0
        else:
            print data['parent_id']
            data['parent_id'] = int(data['parent_id'])
    return data