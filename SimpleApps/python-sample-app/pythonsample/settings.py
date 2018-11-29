# -*- coding: utf-8 -*-
# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.8/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = '*9t@=l3t&3u_@3=^@%=k$tjv+^7(i1h*e^(*f^vcw!8@q09as_'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = []

# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'rest_framework',
    'appsample',
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'django.middleware.security.SecurityMiddleware',
)

ROOT_URLCONF = 'pythonsample.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [os.path.join(BASE_DIR, 'static/templates'), ],
    }, 
    {
        'BACKEND': 'django.template.backends.jinja2.Jinja2',
        'DIRS': [os.path.join(BASE_DIR, 'static/jinja2'), ],
    },
]

WSGI_APPLICATION = 'pythonsample.wsgi.application'

#vcap 설정에서 값 가져오기
import json 
if 'VCAP_SERVICES' in os.environ:
    vcap_services = json.loads(os.environ['VCAP_SERVICES'])
    
    #mysql 세팅
    if 'Mysql-DB' in vcap_services:
        mysql_srv = vcap_services['Mysql-DB'][0]
        mysql_cred = mysql_srv['credentials']

        DATABASES = {
            'default': {
                'ENGINE': 'django.db.backends.mysql',  
                'NAME': mysql_cred['name'],  
                'USER': mysql_cred['username'],  
                'PASSWORD': mysql_cred['password'],  
                'HOST': mysql_cred['hostname'],  
                'PORT': mysql_cred['port'],  
            }, 
        }
    
    #redis 세팅
    if 'redis-sb' in vcap_services:
        redis_srv = vcap_services['redis-sb'][0]
        redis_cred = redis_srv['credentials']
        
        CACHES = {
            "default": {
                "BACKEND": "redis_cache.RedisCache",
                "LOCATION": str(redis_cred['host'])+":"+str(redis_cred['port']),
                "OPTIONS": {
                    "CLIENT_CLASS": "django_redis.client.DefaultClient",
                    'PASSWORD': redis_cred['password'],
                },
            },
        }

        SESSION_ENGINE = "django.contrib.sessions.backends.cache"
        SESSION_CACHE_ALIAS = "default"


    #cubrid credentials 값 획득
    if 'CubridDB' in vcap_services:
        cubrid_srv = vcap_services['CubridDB'][0]
        CUBRID_CRED = cubrid_srv['credentials']

    #mongodb credentials 값 획득
    if 'Mongo-DB' in vcap_services:
        mongo_srv = vcap_services['Mongo-DB'][0]
        MONGO_CRED = mongo_srv['credentials']

    #rabbitmq credentials 값 획득
    if 'p-rabbitmq' in vcap_services:
        rabbitmq_srv = vcap_services['p-rabbitmq'][0]
        RABBITMQ_CRED = rabbitmq_srv['credentials']

    #gluster credentials 값 획득
    if 'glusterfs' in vcap_services:
        gluster_srv = vcap_services['glusterfs'][0]
        GLUSTERFS_CRED = gluster_srv['credentials']
        

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'Asia/Seoul'

USE_I18N = True

USE_L10N = True

USE_TZ = True

STATIC_ROOT = 'staticfiles'
STATIC_URL = '/resources/'

STATIC_BASE_DIR = os.path.dirname(os.path.abspath(__file__))
STATICFILES_DIRS = (
    os.path.join(STATIC_BASE_DIR, '../static/resources'),
)