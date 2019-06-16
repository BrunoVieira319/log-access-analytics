#!/usr/bin/env bash

./gradlew clean build
sudo service mongod start
./gradlew run --parallel