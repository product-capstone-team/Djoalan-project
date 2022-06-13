This folder need only to be stored with ML file
==
## Requirement :
- [Buycott] (https://www.buycott.com/)
- [Python] (https://www.python.org/)
- [Tensorflow] (https://www.tensorflow.org/api_docs/python)

## Dataset
we use Buycott Dataset as input model Convolutional Neural Network (CNN). We took more than 4000 data images of barcodes from the buycott site containing line patterns and sequence numbers of collection products that were available at retail.

![00000000129220](https://user-images.githubusercontent.com/89289597/173224825-23114d06-c109-428d-9819-1636ac30eb28.png)


## Data Preprocessing
We make a dataframe that consists of the image directory and image label. Then we resize the image, change it to grayscale to reduce the memory, cropping the image and making it to array format. After that we split the training and testing for the training and validation purpose.

## Building Model
We perform object recognition of a sequence barcode number using tensorflow in the python environment to create CNN model that solves multilabel classification problems.

## Result
Our model achieves an accuracy of 99% using CNN

![visual_model (1)](https://user-images.githubusercontent.com/89289597/173224795-95a40cc5-eeca-4858-addd-ecd6c15d5ce6.png)

## Convert to TFLite Format
After Building the model, we we convert it to the saved model format. Because we want to deploy it in a mobile application, then we should convert it to TF lite format.

