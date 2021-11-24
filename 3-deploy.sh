#!/bin/bash
set -eo pipefail
ARTIFACT_BUCKET=$(cat bucket-name.txt)
TEMPLATE=template.yml
if [ $1 ]
then
  if [ $1 = mvn ]
  then
    TEMPLATE=template-mvn.yml
    mvn package
  fi
else
  ./gradlew build -i
fi

export AWS_PROFILE=default
AWS_PROFILE=default aws cloudformation package --template-file $TEMPLATE --s3-bucket $ARTIFACT_BUCKET --output-template-file out.yml
AWS_PROFILE=default aws cloudformation deploy --template-file out.yml --stack-name blank-java --capabilities CAPABILITY_NAMED_IAM
