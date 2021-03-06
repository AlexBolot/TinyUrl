#!/usr/bin/env bash

killbg() {
  for p in "${pids[@]}"; do
    kill "$p"
  done
}

trap killbg EXIT
pids=()

project_id=tinypoly-257609
host=localhost
serverPort=8081
emulatorPort=8082
server=http://${host}:${serverPort}

newAccount="{\"email\":\"fabrice.huet@gmail.com\"}"
logEntry1="{\"ptitu\":\"1725396150\",\"author\":\"fabrice.huet@gmail.com\",\"accessIP\":\"51.91.110.78\",\"timestamp\":\"1573672204\",\"status\":\"SUCCESS\"}"
logEntry2="{\"ptitu\":\"1725396150\",\"author\":\"hugo.ojvind.françois@gmail.com\",\"accessIP\":\"12.34.110.78\",\"timestamp\":\"1573672204\",\"status\":\"SUCCESS\"}"
logEntry3="{\"ptitu\":\"1725396150\",\"author\":\"hugo.ojvind.françois@gmail.com\",\"accessIP\":\"21.91.110.78\",\"timestamp\":\"1573656704\",\"status\":\"SUCCESS\"}"
logEntry4="{\"ptitu\":\"1725396150\",\"author\":\"hugo.croenne@gmail.com\",\"accessIP\":\"89.91.110.78\",\"timestamp\":\"1573672204\",\"status\":\"SUCCESS\"}"

## ------------------------------------------------------

export DATASTORE_EMULATOR_HOST=${host}:${emulatorPort}
export DATASTORE_PROJECT_ID=${project_id}
export DATASTORE_USE_PROJECT_ID_AS_APP_ID=true

printf "\n-- Starting emularor (no store on disk, port %s) - (should take 6 seconds) --\n" "${emulatorPort}"
gcloud beta emulators datastore start --no-store-on-disk --host-port=${host}:${emulatorPort} > /dev/null 2>&1 &
pids+=($!)

sleep 6
printf "   - Done -"


printf "\n-- Starting Springboot App at %s (should take 30 seconds) --\n" ${server}
mvn spring-boot:run &#> /dev/null &
pids+=($!)

sleep 30
printf "   - Done -"

printf "\n-- Ping server for healthcheck --\n"
curl -X GET -H "Content-type: application/json" ${server}/healthcheck
#printf "\n-- Does account with ID 0 exists ? --\n"
#curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
#printf "\n-- Creating a new Account --\n"
#curl -X POST -H "Content-type: application/json" ${server}/administration/account/ -d "${newAccount}"
#printf "\n-- Does account with ID 0 now exists ? --\n"
#curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
#echo
echo
printf "\n-- Adding 4 new log entries --\n"
curl -X POST -H "Content-type: application/json" ${server}/logs/add -d "${logEntry1}"
echo
curl -X POST -H "Content-type: application/json" ${server}/logs/add -d "${logEntry2}"
echo
curl -X POST -H "Content-type: application/json" ${server}/logs/add -d "${logEntry3}"
echo
curl -X POST -H "Content-type: application/json" ${server}/logs/add -d "${logEntry4}"
echo
printf "\n-- There should be 1 log for ptitu 1725396150
 --\n"
curl -X GET -H "Content-type: application/json" ${server}/logs/accessByPtitu/1725396150

printf "\n-- There should be 2 logs for ptitu 1725396150
 --\n"
curl -X GET -H "Content-type: application/json" ${server}/logs/accessByPtitu/1725396150

echo
echo
echo
curl -X GET -H "Content-type: application/json" ${server}/administration/account/ptitu/details/1725396150


printf "\n\n-- Tearing down emulator and Springboot application --\n\n"

lsof -t -i tcp:8082 | xargs kill

printf "\n-- All done...\n"