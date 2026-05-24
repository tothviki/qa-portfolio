#!/bin/bash

echo "Running Postman API Tests..."

mkdir -p postman-api-tests/build/reports/postman

for collection in \
  "authentication-api-tests.postman_collection.json:authentication" \
  "booking-api-tests.postman_collection.json:booking" \
  "public-api-tests.postman_collection.json:public-api"; do
  collection_file="${collection%%:*}"
  report_name="${collection##*:}"

  npx --yes newman run "postman-api-tests/collections/${collection_file}" \
    --environment postman-api-tests/environments/production.postman_environment.json \
    --reporters cli,json,html \
    --reporter-json-export "postman-api-tests/build/reports/postman/${report_name}-newman-report.json" \
    --reporter-html-export "postman-api-tests/build/reports/postman/${report_name}-newman-report.html" \
    --iteration-count 1 \
    --verbose
done

echo "Tests completed. Reports saved to postman-api-tests/build/reports/postman"
