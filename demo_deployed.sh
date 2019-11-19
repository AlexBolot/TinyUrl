#!/usr/bin/env bash


server=https://tinypoly-257609.appspot.com/
newAccount="{\"email\":\"fabrice.huet@gmail.com\"}"

## ------------------------------------------------------

printf "\n-- Ping server for healthcheck --\n"
curl -X GET -H "Content-type: application/json" ${server}/healthcheck
printf "\n-- Does account with ID 0 exists ? --\n"
curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
printf "\n-- Creating a new Account --\n"
curl -X POST -H "Content-type: application/json" ${server}/administration/account/ -d "${newAccount}"
printf "\n-- Does account with ID 0 now exists ? --\n"
curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
echo
