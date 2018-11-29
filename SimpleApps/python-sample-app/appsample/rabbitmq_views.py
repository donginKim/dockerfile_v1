# -*- coding: utf-8 -*-
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from django.conf import settings
import pika


def make_connection():
    if settings.RABBITMQ_CRED:
        credentials = settings.RABBITMQ_CRED
        rabbitmq_protocols = credentials['protocols']
        rabbitmq_uri = rabbitmq_protocols['amqp+ssl']['uri']
        parameters = pika.URLParameters(rabbitmq_uri)
        connection = pika.BlockingConnection(parameters)
        return connection
    else:
        return JSONResponse({'errors':'Rabbit MQ service not bound'})


class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

# RabbitMQ에 조직의 변경정보 보내기
def sendOrg(org_id, db_type, msg):
    connection = make_connection()
    
    if type(connection)==JSONResponse:
        return connection

    channel = connection.channel()

    # Queu 명칭은 db타입과 조직ID
    QUEUE = str(org_id) +'_'+ db_type

    channel.queue_declare(queue=QUEUE) # 큐 생성
    channel.basic_publish(exchange='', routing_key=QUEUE, body=msg) #메시지와 메시지가 도달해야할 큐를 지정

    connection.close()

    return True

# RabbitMQ에 해당 조직의 변경사항이 있는지 확인
@csrf_exempt
def receiveOrg(request, org_id, db_type):
    connection = make_connection()

    if type(connection)==JSONResponse:
        return connection

    channel = connection.channel()
    
    QUEUE = str(org_id) +'_'+ db_type # 센드와 리시브중 어느것이 먼저 실행될지 모르니 리시브에서도 큐생성
    msg ='NO_CHANGES'
    
    def endCallback():
        channel.stop_consuming()

    #메시지를 받기 위해 1초간 기다림. 없을 경우 메시지가 존재하지 않을 때, 무한대기 한다.
    connection.add_timeout(1, endCallback)
    
    consume = channel.consume(QUEUE)

    try:
        for method_frame, properties, msg in consume:
            channel.basic_ack(method_frame.delivery_tag)
            if method_frame.delivery_tag == 1:
                break

    #queue가 존재하지 않을 때, 발생하는 예외를 처리 
    except pika.exceptions.ChannelClosed as e:
        pass

    connection.close()
    return JSONResponse({"status":msg},status=200)