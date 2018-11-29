#!/bin/bash

########################################
# docker container auto install nodejs #
########################################

apt-get install curl

curl -sL https://deb.nodesource.com/setup_11.x | bash -

apt-get install -y nodejs


node -v
npm -v