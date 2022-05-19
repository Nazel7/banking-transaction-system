
## How To Run

Clone Project from Github
```
 git clone https://github.com/Nazel7/banking-transaction-system.git

```

Setup Postgresql Database 

SetUp JWT secret 

All in the Environment variable.

## Run as Docker image

You can also run as a docker image 
but will need to run posgresql as a standalone image and setup properties 
directly in application.yml file.

## URL to APis collections:
```
https://www.getpostman.com/collections/93342828ceaaa2cdd8a1

```
## SAMPLE ENV VARIABLE

```
RABBITMQ_EXCHANGE=sankore-notification-ex-out
RABBITMQ_ROUTE_KEY=sankore-notification-key-out
RABBITMQ_PASSWORD=guest
RABBITMQ_USERNAME=guest
RABBITMQ_QUEUE=sankore-notification-queue
POSTGRE_URL=jdbc:postgresql://localhost:5432/sankore_transaction_service;
POSTGRE_USERNAME=postgres
POSTGRE_PASSWORD=mkb-password
PORT:80=8087;RABBITMQ_PORT:56=5672
JWT_SECRET=$2a$10$poqgI6yBmhiTAnacv/.tv.DzczAg.efmg.zlXIagMPhZ3M.OVDip2
FAIL_TRANSACTION_MESSAGE=Transaction fail, please try again. Thank you for banking with us
SUCCESS_TRANSACTION_MESSAGE=Transaction is successful
```



Thank you.

