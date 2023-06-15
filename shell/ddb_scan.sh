#!/bin/bash

main(){
  if [[ -z "${REGION}" ]]; then
    echo "REGION is not set. Setting region to us-west-2"
    export REGION="us-west-2"
  fi

  if [[ -z "${TABLE_NAME}" ]]; then
    echo "TABLE_NAME is not set."
    return 1
  fi

  aws dynamodb scan --table-name ${TABLE_NAME} --region ${REGION} --output json
}
main


