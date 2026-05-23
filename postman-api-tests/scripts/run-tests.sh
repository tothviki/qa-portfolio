#!/bin/bash

echo "Running Postman API Tests..."

npx --yes newman run postman-api-tests/collections/Restful-Booker-API-Tests.json \
  --environment postman-api-tests/environments/production.postman_environment.json \
  --reporters cli,json,html \
  --reporter-json-export postman-api-tests/build/reports/postman/newman-report.json \
  --reporter-html-export postman-api-tests/build/reports/postman/newman-report.html \
  --iteration-count 1 \
  --verbose

echo "Tests completed. Report saved to postman-api-tests/build/reports/postman/newman-report.html"
