# -*- coding: utf-8 -*-
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import MultiPartParser
from rest_framework.views import APIView
from swiftclient.exceptions import ClientException
import time
from django.conf import settings
import swiftclient


def make_connection():
    if settings.GLUSTERFS_CRED:
        credentials = settings.GLUSTERFS_CRED
        auth_url = credentials['auth_url']
        username = credentials['username']
        password = credentials['password']
        tenantname = credentials['tenantname']
        connection = swiftclient.client.Connection(
            auth_version='2',
            authurl=auth_url,
            user=username,
            key=password,
            tenant_name=tenantname
            )
        return connection
    else:
        return JSONResponse({'errors':'GlusterFS service not bound'})

class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

class UploadView(APIView):

    parser_classes = (MultiPartParser,)

    def post(self, request, format=None):
        connection = make_connection()

        if type(connection)==JSONResponse:
            return connection

        if 'file' in request.FILES:

            file = request.FILES['file']
            filename = file._name
            filename = str(filename)
            container_name = 'python-container'
            
            #컨테이너가 존재하지 않을 때, 컨테이너 생성
            try:
                connection.get_container(container_name)
            except ClientException as e:
                if e.message == 'Container GET failed':
                    #'x-container-read' 옵션값을 '.r:*'로 주어 컨테이너 내의 오브젝트들에 대한 접근을 허용한다.
                    connection.put_container(container_name,{'x-container-read':'.r:*'})
                else:
                    raise e

            timestamp = str(time.time()).replace('.','')
            filename = timestamp+'_'+filename
            connection.put_object('python-container',filename, file)

            storage_url = connection.get_auth()[0]
            print storage_url
            resource_path = storage_url+'/'+container_name+'/'+filename

            connection.close()

            return JSONResponse({'thumb_img_path': resource_path},status=200)
        return JSONResponse({'message':'file not found'},status=500)