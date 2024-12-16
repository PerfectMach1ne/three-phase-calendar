#!/usr/bin/bash

curl -v 192.168.1.177:8057/teapot -H 'Content-Type: application/json' -H "X-Powered-By: ReqBin HTTP Client" -d '{"city": "Świdnik"}'
