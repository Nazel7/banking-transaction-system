
## How To Run

1. Clone Project from Github
```
 git clone https://github.com/Nazel7/banking-transaction-system.git

```

2. Setup Postgresql Database

3. SetUp JWT secret 

4. All properties in the Environment variable below

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
POSTGRE_URL=jdbc:postgresql://localhost:5432/sankore_transaction_service
POSTGRE_USERNAME=postgres
POSTGRE_PASSWORD=mkb-password
PORT:80=8088
RABBITMQ_PORT:56=5672
JWT_SECRET=$2a$10$poqgI6yBmhiTAnacv/.tv.DzczAg.efmg.zlXIagMPhZ3M.OVDip2
FAIL_TRANSACTION_MESSAGE=Transaction fail, please try again. Thank you for banking with us
SUCCESS_TRANSACTION_MESSAGE=Transaction is successful
BANK_CODE=000012
INVESTMENT_SCHEDULER=* 0 */25 * * *
INVESTMENT_ACTIVATION_SCHEDULER=* 0 */24 * * *
```

## RUN RABBITMQ IMAGE
```
$ docker run -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management-alpine
```
## BUILD IMAGE

```
Run on project directory in Order below
Supply the environment variable for direct access if running through command line
or setup env while the application can package with the env at runtime. if on intellij 
supply the environment in your environment variable and just click the run button after setting the profile.

NOTE: Each (application-[OPTION].yml ) file stands for different environment. Default is dev

1. ./mvnw spring-boot:run

2. $ ./mvnw clean install -DskipTests

3. $ docker build -t bank-service:latest .
```