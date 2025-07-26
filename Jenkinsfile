pipeline {
    agent any
    
    tools {
        maven '3.9'
        jdk '21'
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
