#!/bin/bash

# https://cloud.google.com/community/tutorials/managing-gcp-projects-with-terraform

export TF_VAR_org_id=47605122706
export TF_VAR_billing_account=0175F4-5C2278-B5A240
# 00BBFB-B3863F-F21F80
export TF_ADMIN=${USER}-terraform-admin6
export TF_CREDS=~/.config/gcloud/${USER}-terraform-admin.json

gcloud projects create ${TF_ADMIN} \
  --organization ${TF_VAR_org_id} \
  --set-as-default

gcloud beta billing projects link ${TF_ADMIN} \
  --billing-account ${TF_VAR_billing_account}

gcloud iam service-accounts create terraform \
  --display-name "Terraform admin account"

gcloud iam service-accounts keys create ${TF_CREDS} \
  --iam-account terraform@${TF_ADMIN}.iam.gserviceaccount.com

gcloud projects add-iam-policy-binding ${TF_ADMIN} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/viewer

gcloud projects add-iam-policy-binding ${TF_ADMIN} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/storage.admin

gcloud services enable cloudresourcemanager.googleapis.com
gcloud services enable cloudbilling.googleapis.com
gcloud services enable iam.googleapis.com
gcloud services enable compute.googleapis.com

gcloud organizations add-iam-policy-binding ${TF_VAR_org_id} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/resourcemanager.projectCreator

gcloud organizations add-iam-policy-binding ${TF_VAR_org_id} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/billing.user

gsutil mb -p ${TF_ADMIN} gs://${TF_ADMIN}

cat > backend.tf << EOF
terraform {
 backend "gcs" {
   bucket  = "${TF_ADMIN}"
   prefix  = "terraform/state"
   project = "${TF_ADMIN}"
 }
}
EOF

gsutil versioning set on gs://${TF_ADMIN}

export GOOGLE_APPLICATION_CREDENTIALS=${TF_CREDS}
export GOOGLE_PROJECT=${TF_ADMIN}

# PROJECT_ID=aharya-staging
# SERVICE_ACCOUNT_NAME=terraform-sa

export TF_VAR_project_name=${USER}-test-compute
export TF_VAR_region=northamerica-northeast1

terraform init
terraform plan
terraform apply -auto-approve
export instance_id=$(terraform output instance_id)
export project_id=$(terraform output project_id)

gcloud compute ssh ${instance_id} --project ${project_id}

terraform destroy -auto-approve
gcloud projects delete ${TF_ADMIN}
gcloud organizations remove-iam-policy-binding ${TF_VAR_org_id} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/resourcemanager.projectCreator

gcloud organizations remove-iam-policy-binding ${TF_VAR_org_id} \
  --member serviceAccount:terraform@${TF_ADMIN}.iam.gserviceaccount.com \
  --role roles/billing.user
#
## gcloud init --project ${PROJECT_ID}
#
#gcloud config set project ${PROJECT_ID}
## gcloud config get-value project
#
#gcloud services enable iam.googleapis.com
## gcloud services enable cloudresourcemanager.googleapis.com
#gcloud services enable serviceusage.googleapis.com
#
#gcloud iam service-accounts describe ${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com
#status=$?
### take some decision ##
#[ $status -eq 0 ] && \
#gcloud iam service-accounts delete ${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com\
#    --quiet
#
#gcloud iam service-accounts create ${SERVICE_ACCOUNT_NAME}\
#    --display-name "Terraform Service Account"\
#    --quiet
#
#gcloud projects add-iam-policy-binding ${PROJECT_ID}\
#    --member "serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com"\
#    --role "roles/cloudsql.client"\
#    --quiet
#
#gcloud iam service-accounts keys create credentials/key.json\
#    --iam-account ${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com\
#    --quiet
#
#
#
#export GOOGLE_APPLICATION_CREDENTIALS=credentials/key.json
#
## terraform init
#terraform apply -auto-approve
