# Tiny Poly

## Getting started

- Install Google Cloud SDK at this address : [https://cloud.google.com/sdk/install](https://cloud.google.com/sdk/install)
- Make sure you are in the google cloud project and that you are login in your machine. Ope a terminal and enter `gcloud auth list`, if your account doesn't appear enter `gcloud auth login`, your default browser will open a login page from google where you can login with your google cloud account
- Set the project id : `gcloud config set project tinypoly-257609`

## Deploy
- Enter : `mvn clean package appengine:deploy`
- To test ether enter : `gcloud app browse` or go to [https://tinypoly-257609.appspot.com/](https://tinypoly-257609.appspot.com/)

## Data Store
- Enter : `gcloud auth application-default login` and login with your google cloud account

## Test our app !
All our requests can also be tested in postman, you can find them in 
demo/SACC.postman_collection.json

## Resources GCloud

In this project, we are using AppEngine for the deployment, and CLoud Datastore to store out data. 

The data we store is :
- Pictures < 4m during 5min (file name, hash, counter, mail creator)
- Account entity (mail, id)
- PtitU entity (hash, mail of owner, counter of time called, url)
- Log file, on file by ptitU

We are using one queue for deleting the images with tasks.

## Postman Request

- https://tinypoly-257609.appspot.com/image/create 
Use with email hugo.ojvind.francois@gmail.com if you created an account using the given requests (or any other account) 
Take any image present on your computer with clearing the value of the file.
