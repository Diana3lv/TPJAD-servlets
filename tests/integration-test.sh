#!/usr/bin/env bash

set -euo pipefail

WAREHOUSE_URL="${WAREHOUSE_URL:-http://localhost:8081/warehouse}"
SHOP_URL="${SHOP_URL:-http://localhost:8082/shop}"

fail() {
    echo "Integration test failed: $1" >&2
    exit 1
}

warehouse_stock="$(curl --silent --show-error --fail "$WAREHOUSE_URL/api/stock")" \
    || fail "Warehouse stock endpoint is unavailable"
python3 -c 'import json, sys; data = json.load(sys.stdin); assert isinstance(data, list) and data' \
    <<<"$warehouse_stock" \
    || fail "Warehouse stock response is not a non-empty JSON array"

approved_response="$(curl --silent --show-error --fail \
    -X POST "$WAREHOUSE_URL/api/stock-requests" \
    -H 'Content-Type: application/json' \
    -d '{"flowerId":"rose-red","quantity":1}')" \
    || fail "Warehouse approval request failed"
[[ "$approved_response" == *'"status":"APPROVED"'* ]] \
    || fail "Warehouse did not approve a valid request"

rejected_response="$(curl --silent --show-error --fail \
    -X POST "$WAREHOUSE_URL/api/stock-requests" \
    -H 'Content-Type: application/json' \
    -d '{"flowerId":"unknown-flower","quantity":1}')" \
    || fail "Warehouse rejection request failed"
[[ "$rejected_response" == *'"status":"REJECTED"'* ]] \
    || fail "Warehouse did not reject an unknown flower"

invalid_status="$(curl --silent --show-error -o /tmp/tpjad-invalid-response.json \
    -w '%{http_code}' -X POST "$WAREHOUSE_URL/api/stock-requests" \
    -H 'Content-Type: application/json' \
    -d '{"flowerId":"rose-red","quantity":0}')"
[[ "$invalid_status" == "400" ]] \
    || fail "Warehouse did not return HTTP 400 for an invalid quantity"
grep -q '"status":"INVALID_REQUEST"' /tmp/tpjad-invalid-response.json \
    || fail "Warehouse invalid-request response is missing its status"
rm -f /tmp/tpjad-invalid-response.json

shop_page="$(curl --silent --show-error --fail "$SHOP_URL/")" \
    || fail "Shop UI is unavailable"
grep -q 'Flower Shop Stock' <<<"$shop_page" \
    || fail "Shop UI did not render its title"

shop_request_page="$(curl --silent --show-error --fail \
    -X POST "$SHOP_URL/" \
    -d 'action=request&flowerId=rose-red&quantity=1')" \
    || fail "Shop request form failed"
grep -q 'Stock request approved' <<<"$shop_request_page" \
    || fail "Shop did not display the approval returned by Warehouse"

echo "Integration tests passed."
