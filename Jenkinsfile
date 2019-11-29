#!groovy
pipeline {
    
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
       
    } //stages
 } 
