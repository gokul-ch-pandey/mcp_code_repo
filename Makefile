
# Makefile for GKE deployment
#
# Targets:
#   all              - Build, push, and deploy the application to GKE
#   build            - Build the Spring Boot jar and Docker image
#   push             - Push the Docker image to Google Container Registry (GCR)
#   gke-enable       - Enable GKE API in your GCP project
#   create-cluster   - Create a GKE cluster
#   get-credentials  - Get kubectl credentials for the GKE cluster
#   deploy           - Deploy the app to GKE using k8s-deployment.yaml
#   logs             - Stream application logs from GKE pods
#   delete-cluster   - Delete the GKE cluster
#
# Variables:
#   PROJECT_ID       - Your GCP project ID
#   CLUSTER_NAME     - Name for the GKE cluster
#   ZONE             - GCP zone for the cluster
#   IMAGE_NAME       - Docker image name
#   GCR_IMAGE        - Full GCR image path

PROJECT_ID ?= gcp-ai-458413
CLUSTER_NAME ?= orders-cluster
ZONE ?= us-central1-a
IMAGE_NAME ?= springboot-orders-app
GCR_IMAGE ?= gcr.io/$(PROJECT_ID)/$(IMAGE_NAME):latest

.PHONY: all build push create-cluster deploy get-credentials logs delete-cluster

all: build push deploy

build:
	mvn clean package -DskipTests
	docker build -t $(GCR_IMAGE) .

push:
	gcloud auth configure-docker
	docker push $(GCR_IMAGE)

gke-enable:
	gcloud services enable container.googleapis.com

create-cluster:
	gcloud container clusters create $(CLUSTER_NAME) --zone $(ZONE) --num-nodes=2

get-credentials:
	gcloud container clusters get-credentials $(CLUSTER_NAME) --zone $(ZONE)

deploy:
	sed 's|<GCR_IMAGE_URL>|$(GCR_IMAGE)|g' k8s-deployment.yaml | kubectl apply -f -

logs:
	kubectl logs -l app=springboot-orders-app --tail=100 -f

delete-cluster:
	gcloud container clusters delete $(CLUSTER_NAME) --zone $(ZONE) --quiet
