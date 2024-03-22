#!/bin/bash

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=ecommerce-sonar \
  -Dsonar.projectName='ecommerce-sonar' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_b2d08c5d82e6a6d94a3be9960c939fcddc94fa43