#!/bin/bash

echo "Running Postman API Tests..."

mkdir -p postman-api-tests/build/reports/postman

npm ci --prefix postman-api-tests

for collection in \
  "authentication-api-tests.postman_collection.json:authentication" \
  "booking-api-tests.postman_collection.json:booking" \
  "public-api-tests.postman_collection.json:public-api"; do
  collection_file="${collection%%:*}"
  report_name="${collection##*:}"

  npm exec --prefix postman-api-tests -- newman run "collections/${collection_file}" \
    --environment "environments/production.postman_environment.json" \
    --reporters cli,json,html \
    --reporter-json-export "build/reports/postman/${report_name}-newman-report.json" \
    --reporter-html-export "build/reports/postman/${report_name}-newman-report.html" \
    --iteration-count 1 \
    --verbose
done

echo "Tests completed. Reports saved to postman-api-tests/build/reports/postman"
