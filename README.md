# Djoalan-Project
## About

The improvement of the retail system is easier, safer, and supports transparency are required in economics nowadays. That’s why we created a mobile application that provides these offers and gives benefits to users and business owners.

**Djoalan** is a self - service system in the form of mobile application built using the kotlin programming language for retail shopping. By utilizing object recognition installed in Djoalan it allows customers to make purchases without needing a cashier


## Feature
Our application provide many feature such as:
1. Authentication ( including register, sign-in, and update account )
2. Bottom Navigation with 4 options of menu
3. Tutorial guidelines: step by step to make a purchase
4. Showcase of lowest price for particular item
5. QR Code scanner
6. Barcode scanner
7. Payment gateway
8. Past user transactions
9. Profile Menu

## Tech Stack
This application was built with:
1. [Kotlin](https://kotlinlang.org/) is a general purpose, free, open source, statically typed “pragmatic” programming language initially designed for the JVM (Java Virtual Machine) and Android that combines object-oriented and functional programming features.
2. [Navigation Component](https://developer.android.com/guide/navigation) is one part of the Jetpack libraries made to support the use of multiple fragments on one activity in mobile development.
3. [Room Database](https://developer.android.com/jetpack/androidx/releases/room) is part of the Jetpack libraries for handling databases in android development. This library is the most fit for Repository Pattern design in android development.
4. [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) is a Jetpack component to store and manage UI-related data in a lifecycle conscious way. The view model component allows data to survive configuration changes such as screen rotations.
5. [Retrofit2](https://square.github.io/retrofit/) is a REST Client for Java and Android allowing to retrieve and upload JSON (or other structured data) via a REST based. You can configure which converters are used for the data serialization, for example GSON for JSON.
6. [Okhttp3](https://square.github.io/okhttp/) is a third-party library developed by Square for sending and receiving HTTP-based network requests.
7. [Coroutine](https://developer.android.com/kotlin/coroutines) is a to simplify code that executes asynchronously.
8. [Camerax](https://developer.android.com/training/camerax) is a jetpack library, built to help make camera app development easier.
9. [ML-kit](https://developers.google.com/ml-kit) is a mobile SDK that brings Google on device machine learning expertise to  Android apps.
10. [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
11. [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection by providing containers for every Android class in your project and managing their lifecycles automatically.
12. [Glide](https://github.com/bumptech/glide) is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.
13. [TensorFlow Lite](https://www.tensorflow.org/lite) is a mobile library for deploying models on mobile, microcontrollers and other edge devices.

## Google cloud services: 
- #### Cloud Run 
Cloud Run is a serverless application, used as a service to deploy the API.
- #### Cloud Storage 
Use as a database to store images 
- #### Google Compute Engine
Runs a virtual machine containing MongoDB server, use by its external IP address

### Database: 
- #### MongoDB
Used to store data for the application, consisting of  4 collections(tables) users, items, transactions, & requestPayment.

### API: 
- #### NodeJs (Hapi)
  - Dependencies: 
    - @google-cloud/storage: ^6.0.1
    - @hapi/cookie: ^11.0.2
    - @hapi/hapi: ^20.2.2
    - @hapi/joi: ^17.1.1
    - hapi-mongodb: ^10.0.3
    - joi-objectid: ^4.0.2
    - mongoose: ^6.3.4
    - xendit-node: ^1.21.3

- #### Postman
Use for API testing

### Optional : 
- #### Docker Desktop 
Used to containerize API by creating a dockerfile to test whether the docker image functions properly before deploying. 

### Flow Chart:
![Flowchart CC drawio](https://user-images.githubusercontent.com/75570657/173223922-1ac1dbcb-6228-482b-aebd-7aee496b5472.png)

## Machine Learning
### Requirement :
- [Buycott] (https://www.buycott.com/)
- [Python] (https://www.python.org/)
- [Tensorflow] (https://www.tensorflow.org/api_docs/python)

### Dataset
we use Buycott Dataset as input model Convolutional Neural Network (CNN). We took 6000 data images of barcodes from the buycott site containing line patterns and sequence numbers of collection products that were available at retail.

![00000000129220](https://user-images.githubusercontent.com/89289597/173224825-23114d06-c109-428d-9819-1636ac30eb28.png)


###Data Preprocessing
We make a dataframe that consists of the image directory and image label. Then we resize the image, change it to grayscale to reduce the memory, cropping the image and making it to array format. After that we split the training and testing for the training and validation purpose.

### Building Model
We perform object recognition of a sequence barcode number using tensorflow in the python environment to create CNN model that solves multilabel classification problems.

### Result
Our model achieves an accuracy of 99% using CNN

![visual_model (1)](https://user-images.githubusercontent.com/89289597/173224795-95a40cc5-eeca-4858-addd-ecd6c15d5ce6.png)

### Convert to TFLite Format
After Building the model, we we convert it to the saved model format. Because we want to deploy it in a mobile application, then we should convert it to TF lite format.


