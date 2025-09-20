pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "ameersr1997/employee-management"
        DOCKER_TAG = "latest"
    }

    triggers {
        githubPullRequest() // triggers on PR merge (GitHub plugin required)
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Al-AmeerSR/employee-management-backend.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application using Maven...'
                bat 'mvn clean package -DskipTests=false'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // publish test results
                }
            }
        }

        stage('Approval for Deployment') {
            steps {
                script {
                    timeout(time: 1, unit: 'HOURS') {
                        input message: 'Deploy to Docker Hub?', ok: 'Deploy'
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-creds') {
                        def appImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        appImage.push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
