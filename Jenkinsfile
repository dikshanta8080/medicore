pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        DOCKER_HUB_REPO = 'dikshanta8080/hospital-management'
        IMAGE_TAG       = 'latest'
        DOCKER_CREDS    = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_HUB_REPO}:${IMAGE_TAG} -t ${DOCKER_HUB_REPO}:${BUILD_NUMBER} ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    sh "echo \$DOCKER_CREDS_PSW | docker login -u \$DOCKER_CREDS_USR --password-stdin"
                    sh "docker push ${DOCKER_HUB_REPO}:${IMAGE_TAG}"
                    sh "docker push ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                }
            }
        }
    }

    post {
        always {
            script {
                sh "docker logout"
                sh "docker rmi ${DOCKER_HUB_REPO}:${IMAGE_TAG} ${DOCKER_HUB_REPO}:${BUILD_NUMBER} || true"
            }
        }
    }
}