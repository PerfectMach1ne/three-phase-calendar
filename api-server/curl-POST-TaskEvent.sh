#!/bin/bash

curl -v -X POST 127.0.0.1:8057/testTask -d "{\"name\": \"Example tasky!\", \"desc\": \"Description! hello this is a description of a task event blaablabalbalbabla\", color: { \"hasColor\": true, \"hex\": 9633782 } }" -H "Content-Type: application/json"
