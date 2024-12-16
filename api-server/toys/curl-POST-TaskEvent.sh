#!/usr/bin/bash

curl -v -X POST 192.168.1.177:8057/api/cal/task -H "Content-Type: application/json" -d "{ \"datetime\": \"2024-10-08T17:00:00\", \"name\": \"Example tasky!\", \"desc\": \"Description! hello this is a description of a task event blaablabalbalbabla\", color: { \"hasColor\": true, \"hex\": \"573849\", \"isdone\": false } }" 
