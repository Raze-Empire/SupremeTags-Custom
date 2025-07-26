/*
 * Jenkins Pipeline for SupremeTags-Custom Java Plugin
 * 
 * This pipeline builds the SupremeTags Minecraft plugin using Maven and archives the resulting JAR file.
 * 
 * Prerequisites:
 * - Jenkins must have Maven configured as a global tool named 'Maven'
 * - Jenkins must have JDK 8 configured as a global tool named 'JDK8'
 * - Adjust tool names in the 'tools' section if your Jenkins uses different names
 */

pipeline {
    agent any
    
    tools {
        // Use Maven tool configured in Jenkins (adjust name as per your Jenkins configuration)
        maven 'Maven'
        // Use JDK 8 as specified in pom.xml (adjust name as per your Jenkins configuration)
        // Alternative names: 'Java-8', 'openjdk-8', 'jdk-8'
        jdk 'JDK8'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                // Git checkout is handled automatically by Jenkins when pipeline is triggered
                // This stage is included for clarity and can include additional checkout steps if needed
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building SupremeTags plugin with Maven...'
                sh 'mvn clean package'
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                echo 'Archiving plugin JAR file...'
                
                // List all JAR files in target directory for visibility
                sh 'echo "JAR files found in target directory:"; ls -la target/*.jar || echo "No JAR files found"'
                
                // Archive all JAR files from target directory as build artifacts
                archiveArtifacts artifacts: 'target/*.jar', 
                                fingerprint: true,
                                allowEmptyArchive: false
                
                // Provide feedback about the main plugin JAR
                script {
                    if (fileExists('target/SupremeTags-2.0.3.jar')) {
                        echo '✓ Successfully found main plugin JAR: SupremeTags-2.0.3.jar'
                    } else {
                        echo '! Main plugin JAR not found with expected name, but other JARs may be available above'
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Build completed.'
            // Clean workspace after build (optional)
            cleanWs()
        }
        success {
            echo 'Build successful! Plugin JAR archived.'
        }
        failure {
            echo 'Build failed. Check logs for details.'
        }
    }
}