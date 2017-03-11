#! /bin/bash

apt-get update

# Install nodejs
apt-get -y install curl
curl -sL https://deb.nodesource.com/setup_7.x | sudo -E bash -
sudo apt-get install -y nodejs make python
