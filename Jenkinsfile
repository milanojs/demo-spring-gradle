#!groovy
pipeline {

    agent {
        kubernetes {
            label 'gradle-builder'
            defaultContainer 'gradle-builder'
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
 } 
