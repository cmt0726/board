#!/bin/bash

if [[ $# != 2 ]]; then
    echo "Usage: ./run.sh BoardXML CardXML"
    exit 1
fi

java -jar deadwood.jar $1 $2
