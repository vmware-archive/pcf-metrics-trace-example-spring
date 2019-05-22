#!/bin/bash
set -ex

WORKING_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $WORKING_DIR/..
echo $(pwd)

if [ -z "$SUFFIX" ]; then echo "usage: SUFFIX=<APP_NAME_SUFFIX> ./deploy.sh" && exit; fi
APP_DOMAIN=${1:-cfapps.io}
PAYMENTS_APP_NAME="payments-$SUFFIX"
ORDERS_APP_NAME="orders-$SUFFIX"
SHOPPING_CART_APP_NAME="shopping-cart-$SUFFIX"

./gradlew build

cf push $PAYMENTS_APP_NAME \
    -p applications/payments/build/libs/payments-trace-example-0.0.1-SNAPSHOT.jar \
    -m 1GB \
    -k 1024M


PAYMENTS_HOST=$PAYMENTS_APP_NAME.$APP_DOMAIN

cf push $ORDERS_APP_NAME \
    -p applications/orders/build/libs/orders-trace-example-0.0.1-SNAPSHOT.jar \
    -m 1GB \
    -k 1024M \
    --no-start

cf set-env $ORDERS_APP_NAME PAYMENTS_HOST $PAYMENTS_HOST
cf start $ORDERS_APP_NAME

ORDERS_HOST=$ORDERS_APP_NAME.$APP_DOMAIN

cf push $SHOPPING_CART_APP_NAME \
    -p applications/shopping-cart/build/libs/shopping-cart-trace-example-0.0.1-SNAPSHOT.jar \
    -m 1GB \
    -k 1024M \
    --no-start

cf set-env $SHOPPING_CART_APP_NAME ORDERS_HOST $ORDERS_HOST
cf start $SHOPPING_CART_APP_NAME

SHOPPING_CART_HOST=$SHOPPING_CART_APP_NAME.$APP_DOMAIN

echo ""
echo Run \`curl $SHOPPING_CART_HOST/checkout\` to verify that the deployment was successful.
echo ""
