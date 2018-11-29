#!/bin/bash

########################################
# docker container auto install python #
########################################

add-apt-repository -y ppa:jonathonf/python-3.6

apt-get update
apt-get install -y python3.6

python --version