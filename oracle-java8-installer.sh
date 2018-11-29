#!/bin/bash

#############################################
# docker container auto install oracle java #
#############################################

add-apt-repository -y ppa:webupd8team/java

echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections

apt-get update

apt-get -y install oracle-java8-installer

javac -version