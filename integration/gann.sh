#!/bin/bash

## httpie

ids=($(http --check-status POST localhost:8080/first-population | jq -r '.creatures[].id'))

for id in "${ids[@]}"
do
    http --check-status POST localhost:8080/act/$id inputs:='[1.23]'
    http --check-status POST localhost:8080/update-fitness/$id fitness:=123
done

http --check-status POST localhost:8080/next-population
