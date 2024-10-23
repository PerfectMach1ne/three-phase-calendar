#!/bin/bash

curl -v 127.0.0.1:8057/teapot -H 'Content-Type: application/json' -H "X-Powered-By: ReqBin HTTP Client" -d '{"city": "Świdnik"}'
