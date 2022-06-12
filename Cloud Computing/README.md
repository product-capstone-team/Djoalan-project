## Google cloud services: 
#### Cloud Run 
- Cloud Run is a serverless application, used as a service to deploy the API.
#### Cloud Storage 
Use as a database to store images 
#### Google Compute Engine
Runs a virtual machine containing MongoDB server, use by its external IP address

### Database: 
#### MongoDB
Used to store data for the application, consisting of  4 collections(tables) users, items, transactions, & requestPayment.

### API: 
#### NodeJs (Hapi)
- Dependencies: 
-- @google-cloud/storage: ^6.0.1
-- @hapi/cookie: ^11.0.2
-- @hapi/hapi: ^20.2.2
-- @hapi/joi: ^17.1.1
-- hapi-mongodb: ^10.0.3
-- joi-objectid: ^4.0.2
-- mongoose: ^6.3.4
-- xendit-node: ^1.21.3

### Postman
Use for API testing

### Optional : 
#### Docker Desktop 
Used to containerize API by creating a dockerfile to test whether the docker image functions properly before deploying. 


### Flow Chart:

![Flowchart CC drawio](https://user-images.githubusercontent.com/75570657/173223477-ac5d8611-7d8e-444a-825f-601990b43346.png)

