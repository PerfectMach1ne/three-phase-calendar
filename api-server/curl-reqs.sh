#!/bin/bash

curl -v -X POST 127.0.0.1:8057/testTask -d "{ \"json-but\": \"for now it's rly just a string lol\" }" -H "Content-Type: application/json"
