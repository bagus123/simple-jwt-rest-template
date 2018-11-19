#!/bin/bash
mkdir -p logs
mkdir -p pids
nohup java -jar target/simple-jwt-rest-template-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --spring.config.location=config/ > logs/log.txt 2>&1 &
echo $! > pids/pid.file