## 파이썬 2.7.10 (64bit) 설치 - msi

#### 명령프롬프트 열어 파이썬 설치확인
`python`
<br><br>
#### 파이썬 인터프리터 작동하지 않을 경우, 시스템변수 Path 맨 뒤에 다음과 같은 경로 추가
`;C:\Python27;C:\Python27\Scripts`<br>
추가되는 경로는 파이썬 버전 및 파이썬 설치경로에 따라 상이하다.<br>
<br>
#### pip 설치확인
pip는 파이썬 관련 각종 패키지 설치를 지원하는 도구이다.<br>
윈도우 명령프롬프트에서 pip 명령어를 입력하여 설치여부 확인한다.<br>
`pip`<br>
<br>
##### pip가 설치되어 있지 않은 경우
>
파이썬 설치경로의 Scripts폴더로 이동<br>
`cd C:\Python27\Scripts\`<br>
<br>
easy_install 이용하여 pip 설치한다.easy_install은 파이썬 관련 패키지 설치를 지원하는 도구이지만, 일반적으로 또 다른 도구인 pip 설치를 위해서만 사용한다. 파이썬 버전에 따라 pip가 설치되지 않는 경우가 있기 때문이다.<br>
`easy_install pip`<br>
<br>
다시 pip 설치를 확인한다.<br>
`pip`
>

---------------------------------------------------------------------------

## 가상환경 구성

#### virtualenv(가상환경 구성하는 도구) 설치

`pip install virtualenv`
<br>
<br>
#### 가상환경을 생성할 폴더로 이동하여 가상환경 생성

`virtualenv myenv`
<br>
<br>
##### 파이썬 버전별로 가상환경 구성하는 경우 (여러버전의 파이썬이 설치되어 있다면, 가상환경 구성에 사용할 파이썬 경로를 '-p' 옵션을 통해 지정한다.)
>
`virtualenv -p C:\Python34\python.exe myenv34`<br>
`virtualenv -p C:\Python27\python.exe myenv27`<br>
>

<br>
#### 가상환경 실행(activate)
`myenv\Scripts\activate`
<br>
---------------------------------------------------------------------------

## 패키지 설치

#### 장고(Django) 설치

가상환경일 경우 가상환경 실행(activate)한 채로 명령어 입력<br>

`pip install django`
<br>
django 1.8.5 버전이 설치 됨
<br>
<br>
#### 장고(Django) 레스트 프레임워크 설치

`pip install djangorestframework`
<br>
---------------------------------------------------------------------------

## 장고(Django) 어플리케이션 생성

#### 장고(Django) 프로젝트 생성할 폴더로 이동
<br>
`cd c:\django`
<br>
<br>
#### 프로젝트 생성

`django-admin.py startproject pythonsample`<br>

'pythonsample'은 프로젝트명
<br>
<br>
#### 생성된 프로젝트 폴더로 이동

`cd pythonsample`
<br>
<br>
#### appsample 어플리케이션 생성

`python manage.py startapp appsample` 
<br>
<br>
#### 생성된 폴더 구조 확인

`tree /f`
<br>
<br>
#### 실행확인을 위해 서버구동

`python manage,py runserver 0.0.0.0:8080`
<br>
<br>
#### 브라우저로 접속하여 'It worked'를 확인

---------------------------------------------------------------------------

## 개방형 클라우드 플랫폼에 배포

#### 개방형 클라우드 플랫폼 로그인

애플리케이션 배포과정을 진행하기 위해 개방형 클라우드 플랫폼의 사용자 계정으로 로그인한다.
로그인을 하기 이전에 먼저 API Endpoint를 지정한다. 명령어는 아래와 같다. 
<br>
`cf api [Endpoint URL]`
<br>
<br>
Endpoint 지정이 완료되었다면, 로그인 명령어를 통해 로그인한다.
<br>
`cf login –u [user name] –o [org name] –s [space name]`
<br>
<br>

#### 서비스 생성

서비스 생성은 개방형 클라우드 플랫폼에서 제공하는 서비스에 대해서 사용자가 서비스 인스턴스를 생성하는 과정이다. 개방형 클라우드 플랫폼에서 제공하지 않는 서비스에 대해서는 서비스 생성이 불가능하며, 서비스 제공 여부는 플랫폼 관리자(운영자)가 결정한다. 먼저 아래의 명령어를 통해, 사용 가능한 서비스의 목록을 확인한다.
<br>
`cf marketplace`
<br>
<br>
상단의 명령어를 통해 확인한 서비스 목록에서 사용하고자 하는 서비스를 생성한다. 샘플 애플리케이션에서는 MySQL, Cubrid, MongoDB, Resdis, RabbitMQ, GlusterFS 서비스를 사용하므로 6개의 서비스를 생성한다. 서비스 생성 명령어는 다음과 같다.
<br>
`cf create-service SERVICE PLAN SERVICE_INSTANCE [-c PARAMETERS_AS_JSON] [-t TAGS]`
<br>
```
예시)
cf create-service Mysql-DB Mysql-Plan1-10con python-mysql
cf create-service CubridDB utf8 python-cubrid
cf create-service Mongo-DB default-plan python-mongodb
cf create-service redis-sb shared-vm python-redis
cf create-service glusterfs glusterfs-5Mb python-glusterfs
cf create-service p-rabbitmq standard python-rabbitmq
```
※	cf create-service 명령어는 서비스명, 플랜, 서비스 인스턴스명을 순서대로 입력하게 되어 있다. 서비스명과 플랜은 cf marketplace 명령어를 통해 확인하고, 서비스 인스턴스명은 임의의 명칭을 사용한다.
<br>
<br>
#### 애플리케이션 배포

##### requirements.txt 생성
requirements.txt 파일에 python 샘플 애플리케이션 구동에 필요한 패키지들이 정의된다. 개방형 클라우드 플랫폼에서는 애플리케이션이 배포될 때, requirements.txt 파일에 정의된 패키지들을 설치한다. 따라서 requirements.txt 파일이 존재하지 않거나 내용이 잘못 되어 있을 경우, 애플리케이션 실행에 문제가 발생한다. 
<br>
requirements.txt
```
Django==1.8.6
djangorestframework==3.3.1
gunicorn==19.1.1
jinja2==2.8
WhiteNoise==2.0.4
MySQL-python==1.2.3
CUBRID-Python==9.3.0.0001
pymongo==2.8
pika==0.10.0
django-redis-cache==1.6.4
python-keystoneclient==2.0.0
python-swiftclient==2.6.0
```
<br>
##### manifest.yml 생성

manifest.yml
```
---
applications:
- name: python-sample-app      # 애플리케이션 이름
  buildpack: python_buildpack    # 빌드팩 이름
  memory: 512M                # 애플리케이션 메모리 사이즈
  instances: 1                   # 애플리케이션 인스턴스 개수
```
※	manifest.yml 파일은 더 많은 설정을 포함할 수 있지만, 필요한 부분만 기술하였다.
<br>
<br>
##### 애플리케이션 배포

애플리케이션 배포 명령어는 다음과 같다. 별도의 옵션을 넣지 않으면 manifest.yml에 정의된 설정을 사용한다. 서비스가 애플리케이션에 바인드되지 않은 상태로 애플리케이션이 배포되기 때문에 --no-start 옵션을 넣어 애플리케이션이 실행되지 않도록 한다.

`cf push --no-start`
<br>
<br>
#### 서비스 바인드

상단의 [서비스 생성]에서 생성한 서비스와 [애플리케이션 배포]에서 배포한 애플리케이션을 연결하는 것을 서비스 바인드(bind)라고 한다. 서비스 바인드를 통해서 애플리케이션은 서비스에 접근할 수 있는 VCAP_SERVICES 환경설정 정보를 얻을 수 있게 된다.
<br>
`cf bind-service APP_NAME SERVICE_INSTANCE [-c PARAMETERS_AS_JSON]`
```
cf bind-service python-sample-app python-mysql
cf bind-service python-sample-app python-cubrid
cf bind-service python-sample-app python-mongodb
cf bind-service python-sample-app python-redis
cf bind-service python-sample-app python-glusterfs
cf bind-service python-sample-app python-rabbitmq
```
※	cf bind-service 명령어는 바인드할 애플리케이션명과 서비스 인스턴스명을 순서대로 입력하여 사용한다. 이때 '-c' 옵션을 이용해 애플리케이션의 VCAP_SERVICES 환경설정정보에 필요한 값을 추가할 수 있다.
<br>
<br>
#### 애플리케이션 실행
서비스 바인드가 완료되었다면 애플리케이션을 정상적으로 실행시킬 수 있다. 애플리케이션 실행 명령어는 다음과 같다.
<br>
`cf start python-sample-app`
※	MySQL과 Cubrid 서비스는 사용자가 직접 DB에 접속하여 테이블을 먼저 생성해야 사용이 가능하다.
<br><br>

#### 샘플어플리케이션에 관한 보다 상세한 설명은 업로드되어 있는 문서 [python 어플리케이션 개발 가이드 v1.0.docx]를 참조한다.