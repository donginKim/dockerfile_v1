# -*- coding: utf-8 -*-
import unittest
import httplib
import json
import requests

class Python_API_Test(unittest.TestCase):
    global host
    host = 'python-sample-app.115.68.46.30.xip.io'
    global connection
    connection= httplib.HTTPConnection(host)

    # Mysql, Cubrid 테스트 ID
    global test_org_id
    test_org_id = '9999'

    global test_group_id
    test_group_id = '9999'

    global headers
    headers = {'Content-type': 'application/json'}
    # GET 요청은 headers가 string 형태로 입력되어야 하기 때문에 headers=headers 형태로 입력한다.


    
    # MySQL test
    def test_01_MySQL_CREATE_org(self):

        body = '{"id":'+test_org_id+',"label":"test_label","desc":"test_desc","url":"test_url"}'
        connection.request('POST', '/orgs/mysql',body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 201)
        assert response.status ==201


    def test_02_MySQL_CREATE_group(self):

        body = '{"id":'+test_group_id+',"org_id":'+test_org_id+',"parent_id":"" ,"label":"test_group","desc":"test_desc","url":"test_url"}'
        connection.request('POST', '/orgs/'+test_org_id+'/groups/mysql',body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 201)
        assert response.status ==201


    def test_03_MySQL_GET_org_list(self):

        connection.request('GET', '/orgs/mysql', headers=headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_04_MySQL_GET_org_list_detail(self):

        connection.request('GET', '/orgs/'+test_org_id+'/mysql', headers=headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_05_MySQL_UPDATE_org_list_detail(self):

        body = '{"label":"updated_test_org","desc":"update_test_desc","url":"update_test_url"}'
        connection.request('PUT', '/orgs/'+test_org_id+'/mysql',body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 201)
        assert response.status ==201


    def test_06_MySQL_GET_group_list(self):

        connection.request('GET', '/orgs/'+test_org_id+'/groups/mysql', headers=headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_07_MySQL_GET_group_list_detail(self):

        connection.request('GET', '/orgs/'+test_org_id+'/groups/mysql', headers=headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_08_MySQL_UPDATE_group_list_detail(self):

        body = '{"org_id":'+test_org_id+',"parent_id":"" ,"label":"update_test_group","desc":"update_test_desc","url":"update_test_url"}'
        connection.request('PUT', '/orgs/'+test_org_id+'/groups/'+test_group_id+'/mysql', body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 201)
        assert response.status ==201


    def test_09_MySQL_GET_orgchart(self):

        connection.request('GET', '/org-chart/'+test_org_id+'/mysql', headers=headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_10_MySQL_DELETE_group_list_detail(self):

        body = '{}'
        connection.request('DELETE', '/orgs/'+test_org_id+'/groups/'+test_group_id+'/mysql',body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200


    def test_11_MySQL_DELETE_org_list_detail(self):

        body = '{}'
        connection.request('DELETE', '/orgs/'+test_org_id+'/mysql',body, headers)
        response = connection.getresponse()
        connection.close()
        self.assertTrue(response.status, 200)
        assert response.status ==200

    # Cubrid Test

    def test_12_Cubrid_CREATE_org(self):

        body = '{"id":'+test_org_id+',"label":"test_label","desc":"test_desc","url":"test_url"}'
        connection.request('POST', '/orgs/cubrid',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_13_Cubrid_CREATE_group_list(self):

        body = '{"id":'+test_group_id+',"org_id":'+test_org_id+',"parent_id":"" ,"label":"test_label","desc":"test_desc","url":"test_url"}'
        connection.request('POST', '/orgs/'+test_org_id+'/groups/cubrid',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_14_Cubrid_GET_orgchart(self):

        connection.request('GET', '/org-chart/'+test_org_id+'/cubrid', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_15_Cubrid_GET_org_list(self):

        connection.request('GET', '/orgs/cubrid', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_16_Cubrid_GET_org_list_detail(self):

        connection.request('GET', '/orgs/'+test_org_id+'/cubrid', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_17_Cubrid_UPDATE_org_list_detail(self):

        body = '{"label":"updated_test_org","desc":"updated_test_desc","url":"updated_test_url"}'
        connection.request('PUT', '/orgs/'+test_org_id+'/cubrid',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_18_Cubrid_GET_group_list(self):

        connection.request('GET', '/orgs/'+test_org_id+'/groups/cubrid', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_19_Cubrid_GET_group_list_detail(self):

        connection.request('GET', '/orgs/'+test_org_id+'/groups/cubrid', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_20_Cubrid_UPDATE_group_list_detail(self):

        body = '{"org_id":'+test_org_id+',"parent_id":"" ,"label":"updated_test_group","desc":"updated_test_desc","url":"updated_test_url"}'
        connection.request('PUT', '/orgs/'+test_org_id+'/groups/'+test_group_id+'/cubrid', body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_21_Cubrid_DELETE_group_list_detail(self):

        body = ''
        connection.request('DELETE', '/orgs/'+test_org_id+'/groups/'+test_group_id+'/cubrid',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_22_Cubrid_DELETE_org_list_detail(self):

        body = ''
        connection.request('DELETE', '/orgs/'+test_org_id+'/cubrid',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    
    # Mongo DB Test

    def test_23_Mongo_CREATE_org(self):
        body = {"label":"test_org","desc":"test_desc","url":"test_url"}
        url = 'http://'+host+'/orgs/mongo'
        response = requests.post(url, data=json.dumps(body), headers=headers)

        #req.add_header('content-type','application/json')
        dict_res = json.loads(response.content)
        global mongo_test_org_id
        mongo_test_org_id = dict_res.get('org')
        
        response.connection.close()
        self.assertTrue(response.status_code, 201)
        assert response.status_code ==201


    def test_24_Mongo_CREATE_group(self):
        body = {"org_id":mongo_test_org_id,"parent_id":"" ,"label":"test_group","desc":"test_desc","url":"test_url"}
        url = 'http://'+host+'/orgs/'+mongo_test_org_id+'/groups/mongo'
        response = requests.post(url, data=json.dumps(body), headers=headers)

        dict_res = json.loads(response.content)
        global mongo_test_group_id
        mongo_test_group_id = dict_res.get('group')
        response.connection.close()
        
        self.assertTrue(response.status_code, 201)
        assert response.status_code ==201


    def test_25_Mongo_GET_org_list(self):

        url = 'http://'+host+'/orgs/mongo'
        response = requests.get(url, headers=headers)

        #connection.request('GET', '/orgs/mongo', headers=headers)
        #response = connection.getresponse()
        connection.close()
        
        self.assertTrue(response.status_code, 200)
        assert response.status_code ==200

    
    def test_26_Mongo_GET_org_list_detail(self):
        connection.request('GET', '/orgs/'+mongo_test_org_id+'/mongo', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_27_Mongo_UPDATE_org_list_detail(self):
        body = '{"label":"update_test_label","desc":"update_test_desc","url":"update_test_url"}'
        connection.request('PUT', '/orgs/'+mongo_test_org_id+'/mongo',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_28_Mongo_GET_group_list(self):
        connection.request('GET', '/orgs/'+mongo_test_org_id+'/groups/mongo', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_29_Mongo_GET_group_list_detail(self):
        connection.request('GET', '/orgs/'+mongo_test_org_id+'/groups/'+mongo_test_group_id+'/mongo', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_30_Mongo_UPDATE_group_list_detail(self):
        body = '{"org_id":"'+mongo_test_org_id+'","parent_id":"" ,"label":"updated_test_group","desc":"updated_test_desc","url":"updated_test_url"}'
        connection.request('PUT', '/orgs/'+mongo_test_org_id+'/groups/'+mongo_test_group_id+'/mongo', body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 201)
        assert response.status ==201

    def test_31_Mongo_GET_orgchart(self):
        connection.request('GET', '/org-chart/'+mongo_test_org_id+'/mongo', headers=headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)     
        assert response.status ==200

    def test_32_Mongo_DELETE_group_list_detail(self):
        body = '{}'
        connection.request('DELETE', '/orgs/'+mongo_test_org_id+'/groups/'+mongo_test_group_id+'/mongo',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200

    def test_33_Mongo_DELETE_org_list_detail(self):
        body = '{}'
        connection.request('DELETE', '/orgs/'+mongo_test_org_id+'/mongo',body, headers)
        response = connection.getresponse()
        connection.close()

        self.assertTrue(response.status, 200)
        assert response.status ==200
    

if __name__ == '__main__':
    unittest.main()