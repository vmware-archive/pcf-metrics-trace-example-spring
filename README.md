[![Build Status](https://travis-ci.org/pivotal-cf/pcf-metrics-trace-example-spring.svg?branch=master)](https://travis-ci.org/pivotal-cf/pcf-metrics-trace-example-spring)

### DEPLOY
To use the script, you must login as a user that has the ability to assign space permissions and make spaces.
It will create a given shopping-cart, orders, and payments app that can be used to preview an example trace.

To deploy. Use the script `./scripts/deploy.sh` with the following ENV vars:

| ENV        | Desc           |
| ------------- |:-------------:|
| SUFFIX    | Unique identifier for the tracer applications |

#### For example
```
cf login
SUFFIX=test-17 ./scripts/deploy.sh
```

### CURL APPS
Curl the `/checkout` endpoint for the given shopping cart app.

#### For example
```
curl shopping-cart-test-17.cfapps.io/checkout
```

### CLEANUP

To cleanup. Use the script `./scripts/cleanup.sh` with the following ENV vars:

| ENV        | Desc           |
| ------------- |:-------------:|
| SUFFIX    | Unique identifier for the tracer applications used in deploy step |

#### For example
```
cf login
SUFFIX=test-17 ./scripts/cleanup.sh
```
