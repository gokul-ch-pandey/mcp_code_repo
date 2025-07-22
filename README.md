# Spring Boot Orders Application on GKE

## Overview
This project is a sample Spring Boot CRUD application for managing orders, containerized with Docker and deployable to Google Kubernetes Engine (GKE). Application logs are streamed to Google Cloud Logging (Logs Explorer).

## Accessing the Application
After deploying to GKE, the application is exposed via a Kubernetes `LoadBalancer` service. To access it from your local machine:

1. **Get the external IP:**
   ```sh
   kubectl get service springboot-orders-service
   ```
   Look for the `EXTERNAL-IP` in the output. It may take a few minutes to appear after deployment.

2. **Access the API:**
   - Base URL: `http://<EXTERNAL-IP>/orders`
   - Example: `curl http://<EXTERNAL-IP>/orders`


Note - 
gcloud compute firewall-rules create allow-gke-http --allow tcp:80 --target-tags gke-orders-cluster --description="Allow HTTP traffic to GKE nodes"

For testing 
gcloud compute firewall-rules create allow-http-gke --allow tcp:80 --network default

3. **Swagger/OpenAPI:**
   If you add Swagger dependencies, you can access the UI at `http://<EXTERNAL-IP>/swagger-ui.html` (not included by default).

## Logs in Google Cloud
- Application logs are automatically streamed to Google Cloud Logging (Logs Explorer) and can be viewed in the GCP Console under Operations > Logging > Logs Explorer.

## Makefile Usage
See the Makefile for build, push, deploy, and management commands.

---
