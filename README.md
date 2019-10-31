# Tiny Poly

## Getting started

- Install Google Cloud SDK at this address : [https://cloud.google.com/sdk/install](https://cloud.google.com/sdk/install)
- Make sure you are in the google cloud project and that you are login in your machine. Ope a terminal and enter `gcloud auth list`, if your account doesn't appear enter `gcloud auth login`, your default browser will open a login page from google where you can login with your google cloud account
- Set the project id : `gcloud config set project tinypoly-257609`


## Deploy
- Enter : `mvn clean package appengine:deploy`
- To test ether enter : `gcloud app browse` or go to [https://tinypoly-257609.appspot.com/](https://tinypoly-257609.appspot.com/)
