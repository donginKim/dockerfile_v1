##개방형 클라우드 플랫폼 php-sample

개발환경은 개방형 플랫폼의 네트워크의 구성에 따라 로컬에 구성을 하거나 직접 개방형 플랫폼에 Deploy하여 관리할 수 있습니다. 여기서는 Windows 환경에 간단하게 개발환경을 구성하고 개방형 플랫폼에 배포(Deploy)하는 방법을 설명하겠습니다.

###개발환경 구성

PHP 개발환경을 구성하려면 Web Server와 PHP 엔진, Extension 설치등을 해야하는데 이를 간편하게 구성 놓은 툴이 있습니다. 본 가이드에서는 XAMP를 이용하여 설치를 하고 구성하도록 하겠습니다.

본 문서 작성을 위해 구성한 시스템은 아래와 같습니다.
-	OS : Windows 8.1 64bit
-	XAMP PHP 5.5.30
-	Mongo 라이브러리 : 
-	Composer : 

PHP로 REST/full 서버를 구현하였고 화면(HTML)은 Apache의 Web 서버에서 제공을 합니다. HTML과 PHP는 별도로 돌아가는 구조입니다.

###PHP 샘플 소스 받기
샘플의 위치는 변경될수 있느나 개방형 플랙폼 홈페이지에서 찾아볼 수 있습니다. 해당 GIT 위치를 확인하시고 아래와 같은 명령문으로 소스를 다운로드 받습니다. 해당 명령을 위해서는 GIT Client가 설치되어 있어야 합니다.


###XAMP설치
URL (https://www.apachefriends.org/index.html) 에 접속하면 바로 다운로드 화면이 나옵니다. 여기서 “Click here for other version”을 선택합니다. 
 
1. 다운로드는 Windows버전 PHP 5.5.30 (32bit)을 다운로드 받습니다.

2. 다운로드 받은 파일을 실행하고 모두 Next를 하면됩니다. 하지만 아래와 같이 디렉토리를 물어보는데 이때 이 위치를 변경하거나 정확하게 기억하고 있어야 합니다. 설치 완료후 php 실행 디렉토리를 환경변수(Path)에 넣어 줘야 하기 때문입니다.

3. 설치가 정상적으로 이루어지고 있으면 아래와 같이 진행이 될겁니다. 처음 실행할 때 Antivirus 프로그램으로 느려질수 있다는 문구 등이 나올수도 있는데 “확인”을 누르시면 됩니다.

4. 설치가 완료되면 Control Panel을 띄우겠다는 메시지가 나옵니다. 선택이 Default로 되어 있어 완료를 선택하면 아래와 같은 Control Panel이 실행됩니다.

>사용방법은 간단합니다. 원하는 서비스(여기서는 Apache 만 사용할 예정)의 Start를 선택하면 해당 서비스가 실행이 됩니다. 단. 해당서비스가 사용하는 포트(Apache의 경우 80)는 사용하고 있지 않아야 합니다.

####Apache시작위치 변경
Apache의 Config를 선택하고 Apache (httpd.conf)를 선택하여 DocumentRootdhk Directory의 위치를 개발소스가 설치된 곳으로 바꾸면 브라우져에서 http://localhost 로 호출시 개발하는 위치로 바로 연결됩니다. 개발소스의 위치는 2.2.1에서 설치한 위치를 지정해 넣습니다.

```
DocumentRoot C:\개발소스위치

<Directory C:\개발소스위치>
…
```

###PHP 실행 환경설정
XAMP로 설치된 PHP를 어디서나 실행가능하게 환경설정(Path)에 넣어줍니다. 제어판 -> 시스템 -> 고급 시스템 설정을 선택하면 아래와 같이 시스템 속성을 변경하는 창이 나타납니다. 
 
여기서 “환경변수”를 선택하고 Path를 편집을 합니다. 변수값의 마지막에 XAMP 설치 디렉토리 아래의 php 디렉토리를 추가합니다. 


###Path 환경변수 설정

정상적으로 구성이 되었는지 확인하려면 “명령 프롬프트”를 실행하고 php –version 을 선택하여 아래와 같이 나오면 정상적으로 환경설정이 완료된 것입니다.
 
명령 프롬프트에서 PHP 버전 확인
```
c:\php -version
```

###Composer 설치
Composer는 개발시 필요한 라이브러리를 관리하는 툴입니다. 홈페이지는 다음과 같습니다. https://getcomposer.org/

1. Composer를 다운로드 받아 설치하고 Path에 설정하는 방법이 있지만 여기서는 Manual로 설치를 하겠습니다. Composer.phar 파일을 개방형 플랫폼에서 그대로 사용하고 있어 Manual 설치로 composer.phar파일을 개발 위치에 설치하였습니다. (소스를 Git에서 받았으면 따로 설치할 필요는 없습니다.)

2. 매뉴얼 설치는 간단합니다. 아래와 같이 소스의 루트 디렉토리에서 입력을 하면됩니다. 물론 PHP 명령이 실행이 될 수 있게 2.2.3의 환경변수 설정이 되어 있어야만 합니다.
```
php r "readfile('https://getcomposer.org/installer');" | php
```

필요한 Package를 composer.json에 구성하고 install하면 PHP에서 사용할 수 있는 Package가 vendor 디렉토리 아래에 설치가 됩니다. 

>###주의사항
>개방형 플랫폼에서 사용하기 위해서는 XAMP와는 환경이 틀리기 때문에 PHP 빌드팩의 Release된 Extension을 확인하고 Dependency를 확인해야 합니다. XAMP 환경에서는 동작을 하지만 개방형 플랫폼에서는 동작을 안할 수 있으니 주의해야 합니다.<br>
>PHP 빌드팩 Extenstion 정보 : https://github.com/cloudfoundry/php-buildpack/releases

###Mongo 드라이버 설치

1. Mongo 드라이브 설치는 개방형 플랫폼에서 지원하는 Mongo 드라이버를 설치하기 위함입니다. 관련문서는 http://docs.php.net/manual/en/mongo.installation.php#mongo.installation.windows에 있습니다. 라이브러리 파일을 다운받고 config 파일에 정보를 추가하면 됩니다.

2. 먼저 문서에 PECL 홈페이지(http://pecl.php.net/package/mongo)에서 DLL를 다운로드 받아야 합니다. 본 가이드는 1.6.12 버전을 을 선택하였습니다. 
 
3. 위의 링크중 DLL 부분을 눌러서 선택하고 “5.5 Thread Safe (TS) x86”를 다운로드 받습니다. 압축을 해제하면 php_mongo.dll이 있는데 이 파일만 있으면 됩니다.

4. php_mongo.dll 파일을 XAMP 설치된 디렉토리에서 php 아래에 ext에 복사를 합니다.

5. XAMP 설치 디렉토리의 php 디렉토리에 php.ini를 선택하고 아래와 같이 추가를 해줍니다.
'''
extension=php_mongo.dll
'''

6. php.ini 파일의 셋팅이 끝났으면 Apache서버를 재기동합니다. 만약에 오류가 있으면 XAMP 패널에 빨간색으로 오류가 표시되니 정상적으로 Apache서버가 올라오는지 확인합니다.

7. 모듈이 정상적으로 설치되었는지 확인하려면 소스코드의 루트에 있는 info.php를 실행합니다. 브라우져에서 http://localhost/info.php 를 선택하고 내용에 mongo 부분의 설정 정보가 보이면 정상적으로 설치가 된 것입니다.

