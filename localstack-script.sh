#!/bin/bash

awslocal s3api \
create-bucket --bucket $DEFAULT_BUCKET_NAME \
--create-bucket-configuration LocationConstraint=eu-central-1 \
--region eu-central-1