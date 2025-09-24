pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "ameersr1997/employee-management"
        DOCKER_TAG = "latest"
        SONARQUBE = "MySonarQube"  // Jenkins SonarQube configuration name
    }

    stages {

        // -----------------------------
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Al-AmeerSR/employee-management-backend.git'
            }
        }

        // -----------------------------
        stage('Build') {
            steps {
                echo 'Building the application using Maven...'
                // Skipping tests here is optional; we run them in next stage
                bat 'mvn clean package -DskipTests=false'
            }
        }

        // -----------------------------
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests with coverage...'
                bat 'mvn test'
            }
            post {
                always {
                    // Publish test reports
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // -----------------------------
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv("${SONARQUBE}") {
                        // Run SonarQube analysis
                        bat "mvn sonar:sonar -Dsonar.projectKey=employee-management -Dsonar.host.url=${env.SONAR_HOST_URL} -Dsonar.login=${env.SONAR_AUTH_TOKEN} -Dsonar.java.coveragePlugin=jacoco -Dsonar.coverage.jacoco.xmlReportPaths=target/jacoco/jacoco.xml"
                    }
                }
            }
        }

        // -----------------------------
        stage('Quality Gate') {
            steps {
                // Fail the pipeline if quality gate fails
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // -----------------------------
        stage('Approval for Deployment') {
            steps {
                script {
                    timeout(time: 1, unit: 'HOURS') {
                        input message: 'Deploy to Docker Hub?', ok: 'Deploy'
                    }
                }
            }
        }

        // -----------------------------
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

    // -----------------------------
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
