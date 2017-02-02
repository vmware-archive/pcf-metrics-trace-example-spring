#!/bin/bash
set -e

WORKING_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $WORKING_DIR/..
echo $(pwd)

if [ -z "$SUFFIX" ]; then echo "usage: SUFFIX=<APP_NAME_SUFFIX> ./cleanup.sh" && exit; fi

PAYMENTS_APP_NAME="payments-$SUFFIX"
ORDERS_APP_NAME="orders-$SUFFIX"
SHOPPING_CART_APP_NAME="shopping-cart-$SUFFIX"

cf delete $PAYMENTS_APP_NAME -f -r
cf delete $ORDERS_APP_NAME -f -r
cf delete $SHOPPING_CART_APP_NAME -f -r