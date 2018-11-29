# -*- coding: utf-8 -*-
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from django.core.cache import cache
from django.http import HttpResponseRedirect
import uuid

class JSONResponse(HttpResponse):

    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@csrf_exempt
def login(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)

        #로그인 가능한 ID와 Password는 'admin','admin'으로 고정
        if data['id'] == 'admin' and data['password'] =='admin':
            cookie = request.COOKIES.get('login_cookie')
            
            #이미 로그인 된 상태에서 다시 로그인 할 경우에 기존의 키값을 redis에서 삭제한다.
            if cache.get(cookie) == 'admin':
                #redis에서 쿠키 삭제
                cache.delete(cookie)
            
            #random_uuid를 생성하여 redis에 저장 
            random_uuid = uuid.uuid4()
            cache.set(random_uuid,'admin',timeout=500)

            #random_uuid를 login_cookie란 이름으로 쿠키에 저장
            response = JSONResponse({},status=200)
            response.set_cookie('login_cookie',random_uuid)
            return response
        else:
            return JSONResponse({},status=401)

@csrf_exempt
def logout(request):
    if request.method == 'POST':
        cookie = request.COOKIES.get('login_cookie')
        if cache.get(cookie) == 'admin':
        	#redis에서 쿠키 삭제
            cache.delete(cookie)
            
            #쿠키를 삭제한 response를 리턴
            response = JSONResponse({},status=200)
            response.delete_cookie('login_cookie')
            return response
        else:
            return JSONResponse({},status=500)