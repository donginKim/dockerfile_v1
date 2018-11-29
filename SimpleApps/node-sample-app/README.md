

# node-sample-app
------------
## 샘플 설명
OpenPaaS에서 node.js언어를 이용하여 개발할 때 OpenPaaS에서 생성한 서비스에 접근하는 방법을 가이드한다.  

샘플에 포함된 서비스 목록
* mysql
* cubrid
* mongodb
* redis
* rabbitMQ
* glusterfs

## 개발환경 구성
Node.js 개발을 위해서 node.js와 npm을 설치해야 한다.

이 샘플을 개발한 환경은 아래와 같다.
* OS : Windows 7 64bit
* Node.js : v0.12.4
* npm : v2.10.1

Node.js와 npm의 버젼은 CF에서 제공하는 [nodejs-buildpack](https://github.com/cloudfoundry/nodejs-buildpack/blob/master/CHANGELOG) 버젼에 따라 달라질 수 있다.

### Node.js 샘플 소스 받기
'명령 프롬프트'창에서 git clone 명령어를 통하여 샘플 소스를 받을 수 있다. 해당 명령어는 git client가 설치되어 있어야한다.
```
git clone https://github.com/OpenPaaSRnD/OpenPaaSSample/
```

### Node.js 및 npm 설치
URL (https://nodejs.org/dist/) 에 접속하여 설치할 버젼을 선택하여 내려받을 수 있다. Node.js와 npm을 한번에 설치할 수 있다.

1. 원하는 버젼을 내려받는다. 윈도우의 경우 *.msi 확장자로 된 파일을 내려받으면 마법사형태로 설치가 가능하다.  
본 가이드에서는 64bit용 0.12.4버젼을 선택하였다. (https://nodejs.org/dist/v0.12.4/x64/node-v0.12.4-x64.msi)

2. 내려받은 *.msi파일을 실행하고 별다른 설정없이 Next를 눌러 설치를 완료한다.

3. 시작 메뉴에서 '명령 프롬프트'를 검색하여 실행한다.

4. '명령 프롬프트'창에서 아래의 명령어를 이용하여 node.js와 npm이 제대로 설치 되었는지 확인한다.
  ```
  >node -v
  >npm -v
  ```

### OpenPaaS로 배포 및 실행
내려받은 샘플 소스 위치에서 node-sample-app폴더로 이동한 상태에서 OpenPaaS로 배포할 수 있다. 배포 후 미리 생성한 서비스와 연결하여 실행할 수 있다. 이를 위해서 cf cli가 설치되어 있어야하고 서비스는 미리 생성되어 있다고 가정한다.(각 서비스 인스턴스의 이름은 'openpaas-sample-'의 접두사를 붙여 생성했다고 가정한다.)  

```
샘플을내려받은경로> cf push --no-start

샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-mysql
샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-cubrid
샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-mongodb
샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-redis
샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-rabbitmq
샘플을내려받은경로> cf bind-service node-sample-app openpaas-sample-glusterfs

샘플을내려받은경로> cf restart node-sample-app
```
#### 주의사항
node_modules폴더는 설치된 npm모듈들이 들어있다. 이 디렉토리를 제외하지않고 샘플을 배포하면 mongo모듈에서 에러가 발생하기 때문에 꼭 .cfignore 파일에 node_modules가 포함되어 있어야 한다.

## 개발
Node.js에서 많이 사용하는 Express 환경을 구성하는 방법을 가이드한다.

1. 먼저 개발을 진행할 폴더를 하나 생성하고 명령 프롬프트창에서 해당 폴더로 이동한다.
  
  ```
  >cd 생성한폴더경로
  ```
2.  Express 환경을 구성해주는 'express-generator'모듈을 설치한다.

  ```
  생성한폴더경로>npm install express-generator
  ```
3. 설치가 완료되면 'node_modules'라는 폴더가 생성되고 '.bin'폴더 아래에 express라는 실행가능한 파일이 생성된다. 이 파일을 실행하여 express 어플리케이션을 생성한다.  
view enjin을 'ejs'를 사용하려면 -e 옵션을, 'jade'를 사용하려면 옵션없이 생성하면된다. 본 가이드에서는 -e 옵션으로 생성하였다.
  
  ```
  생성한폴더경로>.\node_modules\.bin\express -e
  ```
4. 위 명령을 실행하면 express 어플리케이션 구동이 필요한 기본적인 폴더구조를 생성해주고 필요한 module들의 정보를 package.json파일에 작성해준다. package.json파일의 module들을 설치한다.
  
  ```
  생성한폴더경로>npm install
  ```
5. 설치가 완료되면 아래의 두가지 방법중 하나를 선택하여 어플리케이션을 실행한다.  
package.json에 정의된 명령어로 실행하려면
  
  ```
  생성한폴더경로>npm start
  ```
직접 실행할 파일을 지정하여 실행하려면
  
  ```
  생성한폴더경로>node bin/www 
  ```
6. 제대로 실행되었는지 브라우저에서 
  ```
  http://localhost:3000
  ```
  로 접근하여 확인한다.
  
### 개발도구
어떤 문서편집도구를 사용해도 무방하나 javascript형식을 지원하는 문서편집도구를 사용하는것이 편리하다.
