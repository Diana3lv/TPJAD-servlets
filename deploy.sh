#!/usr/bin/env bash

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOMCAT_WEBAPPS="${TOMCAT_WEBAPPS:-${PROJECT_DIR}/../servers/tomcat/libexec/webapps}"
JETTY_WEBAPPS="${JETTY_WEBAPPS:-${PROJECT_DIR}/../servers/jetty-base/webapps}"

echo "Rebuilding projects..."
(cd "$PROJECT_DIR" && mvn -f warehouse-service/pom.xml clean test package)
(cd "$PROJECT_DIR" && mvn -f shop-service/pom.xml clean test package)

echo "Deploying to servers..."
if [[ ! -d "$TOMCAT_WEBAPPS" || ! -d "$JETTY_WEBAPPS" ]]; then
    echo "Tomcat or Jetty deployment directory does not exist." >&2
    exit 1
fi
cp "$PROJECT_DIR/warehouse-service/target/warehouse.war" "$TOMCAT_WEBAPPS/warehouse.war"
cp "$PROJECT_DIR/shop-service/target/shop.war" "$JETTY_WEBAPPS/shop.war"

echo "Deployment complete."
