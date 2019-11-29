#!groovy
pipeline {
    environment {
          PROJECT_ID = "latamxp-dev"
         
	}
    agent {
        kubernetes {
            label 'gradle-builder'
            yamlFile 'AgentPod.yaml'
        }
    }
    stages {
        stage('Checkout code') {
            steps {
                container('builder') {
                    checkout scm
                }
            }
        }
       
        stage('Deploy Endpoints') {
            steps {
                withCredentials([file(credentialsId: 'endpoints-sanbox-latamxp', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {            
                    container('gcloud-sdk-container') {
                        script {
                            try {
                                sh 'mkdir -p endpoints ; chmod 777 -Rv endpoints'
                                dir("endpoints")
                                    {
                                        git url: 'ssh://git@coderepocsa.appslatam.com:7999/latamxp/poc-ci-dummy-helloworld-deployment.git', credentialsId: 'db0cef85-68dc-4082-8ff4-a41c0faf82a1'
                                        sh """ 
                                        gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS
                                        gcloud auth list
                                        gcloud config get-value project
                                        gcloud config set project ${PROJECT_ID}
                                        gcloud endpoints services deploy dev/endpoints/*.yaml
                                        echo "endpoint creado Correctamente"  
                                        """
                                    }
                            } 
                            catch (Exception e) {
                                echo "Ocurrio un error al crear endpoint"
                                currentBuild.result = 'FAILED'
                            }
                        } // script
                    } //cloud Container
                } // withCredentials
            }// step
        } // stagess
    } //stages
 } 
