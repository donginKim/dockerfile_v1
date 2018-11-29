FROM ubuntu:xenial

COPY ./oracle-java8-installer.sh /home/ubuntu/script/oracle-java8-installer.sh
COPY ./gradle-installer.sh /home/ubuntu/script/gradle-installer.sh
COPY ./nodejs-installer.sh /home/ubuntu/script/nodejs-installer.sh
COPY ./go-lang-installer.sh /home/ubuntu/script/go-lang-installer.sh
COPY ./python-installer.sh /home/ubuntu/script/python-installer.sh

COPY ./SimpleApps/ /home/ubuntu/

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get -y install software-properties-common

RUN /bin/sh /home/ubuntu/script/oracle-java8-installer.sh
RUN /bin/sh /home/ubuntu/script/gradle-installer.sh
RUN /bin/sh /home/ubuntu/script/nodejs-installer.sh
RUN /bin/sh /home/ubuntu/script/go-lang-installer.sh
RUN /bin/sh /home/ubuntu/script/python-installer.sh
