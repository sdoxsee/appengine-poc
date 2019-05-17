# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Reference doc](https://docs.spring.io/spring-cloud-gcp/docs/1.1.0.M3/reference/htmlsingle/)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Samples](https://github.com/spring-cloud/spring-cloud-gcp/tree/master/spring-cloud-gcp-samples)

From https://www.baeldung.com/spring-boot-google-app-engine
```
GCP_USER_EMAIL=user@example.com
PROJECT_ID=aharya-staging-project
CLOUD_SQL_INSTANCE=backend-db
CLOUD_SQL_DATABASE=mysql_db
REGION=northamerica-northeast1
CLOUD_SQL_ROOT_PASSWORD=root
CLOUD_SQL_TIER=db-f1-micro

gcloud components update
gcloud projects create ${PROJECT_ID} --set-as-default
gcloud config set project ${PROJECT_ID}
gcloud projects describe ${PROJECT_ID}
gcloud app create --project=${PROJECT_ID} --region=${REGION}
gcloud components install app-engine-java

gcloud projects add-iam-policy-binding ${PROJECT_ID} --member="user:${GCP_USER_EMAIL}" --role "roles/cloudsql.client" --quiet

gcloud sql tiers list
gcloud sql instances create ${CLOUD_SQL_INSTANCE} --tier=${CLOUD_SQL_TIER} --region=${REGION} --database-version=MYSQL_5_7
gcloud sql users set-password root --host % --instance ${CLOUD_SQL_INSTANCE} --password ${CLOUD_SQL_ROOT_PASSWORD}

gcloud sql databases create ${CLOUD_SQL_DATABASE} --instance ${CLOUD_SQL_INSTANCE}
```
