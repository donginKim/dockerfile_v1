#!/bin/bash

########################################
# docker container auto install gradle #
########################################

apt-get update -y

apt-get -y install gradle
apt-get -y install maven

gradle -v
mvn -v