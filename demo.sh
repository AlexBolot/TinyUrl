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

newAccount="{\"email\":\"bolotalex06@gmail.com\"}"

## ------------------------------------------------------

export DATASTORE_EMULATOR_HOST=${host}:${emulatorPort}
export DATASTORE_PROJECT_ID=${project_id}
export DATASTORE_USE_PROJECT_ID_AS_APP_ID=true

printf "\n-- Starting emularor (no store on disk, port %s) - (should take 6 seconds) --\n" "${emulatorPort}"
gcloud beta emulators datastore start --no-store-on-disk --host-port=${host}:${emulatorPort} > /dev/null 2>&1 &
pids+=($!)

sleep 6
printf "   - Done -"


printf "\n-- Starting Springboot App at %s (should take 12 seconds) --\n" ${server}
mvn spring-boot:run > /dev/null &
pids+=($!)

sleep 12
printf "   - Done -"

printf "\n-- Ping server for healthcheck --\n"
curl -X GET -H "Content-type: application/json" ${server}/healthcheck
printf "\n-- Does account with ID 0 exists ? --\n"
curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
printf "\n-- Creating a new Account --\n"
curl -X POST -H "Content-type: application/json" ${server}/administration/account/ -d "${newAccount}"
printf "\n-- Does account with ID 0 now exists ? --\n"
curl -X GET -H "Content-type: application/json" ${server}/checkid/account/0
echo

printf "\n\n-- Tearing down emulator and Springboot application --\n\n"

lsof -t -i tcp:8082 | xargs kill

printf "\n-- All done...\n"