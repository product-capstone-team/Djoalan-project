This folder need only to be stored with ML file
==
## Requirement :
- [Buycott] (https://www.buycott.com/)
- [Python] (https://www.python.org/)
- [Tensorflow] (https://www.tensorflow.org/api_docs/python)

## Dataset
we use Buycott Dataset as input model Convolutional Neural Network (CNN). We took 6000 data images of barcodes from the buycott site containing line patterns and sequence numbers of collection products that were available at retail.
<picture>
<source media = "(prefers-color-scheme: dark)" srcset = " https://www.buycott.com/upc/8999999199128"
<img alt = "sample data">
</picture>

##Data Preprocessing
We make a dataframe that consists of the image directory and image label. Then we resize the image, change it to grayscale to reduce the memory, cropping the image and making it to array format. After that we split the training and testing for the training and validation purpose.

## Building Model
We perform object recognition of a sequence barcode number using tensorflow in the python environment to create CNN model that solves multilabel classification problems.

## Result
Our model achieves an accuracy of 99% using CNN
<picture>
<source media = "(prefers-color-scheme: dark)" srcset = " https://drive.google.com/file/d/1JWH1ysAIM06qK21HovrVQXubzFu9tWbk/view?usp=sharing"
<img alt = "accuracy">
</picture>

## Convert to TFLite Format
After Building the model, we we convert it to the saved model format. Because we want to deploy it in a mobile application, then we should convert it to TF lite format.
