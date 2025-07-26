pipeline {
    agent {
        dockerContainer { image 'maven:3.9.11-amazoncorretto-21' }
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out repository...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project with Maven...'
                sh 'mvn clean package'
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                echo 'Archiving JAR artifacts...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
