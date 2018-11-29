#!/bin/bash

########################################
# docker container auto install golang #
########################################

#add-apt-repository -y ppa:longsleep/golang-backports

apt-get update

apt-get install -y golang-go

go version