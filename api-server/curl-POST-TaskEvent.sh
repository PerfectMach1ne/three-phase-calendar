#!/bin/bash

curl -v -X POST 127.0.0.1:8057/testTask -H "Content-Type: application/json" -d "{ \"datetime\": \"2024-10-08T17:00:00\", \"name\": \"Example tasky!\", \"desc\": \"Description! hello this is a description of a task event blaablabalbalbabla\", color: { \"hasColor\": true, \"hex\": 9633782 } }" 
