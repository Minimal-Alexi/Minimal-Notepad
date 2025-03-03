pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Minimal-Alexi/Minimal-Notepad.git'
            }
        }
        stage('Inject .env') {
            steps {
                withCredentials([file(credentialsId: 'minimal-notepad-env-file', variable: 'ENV_FILE')]) {
                    bat "copy %ENV_FILE% .env"
                }
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Test & Coverage') {
            steps {
                bat 'mvn test jacoco:report' // Runs tests & generates JaCoCo coverage report
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Publish JUnit test results
                    jacoco execPattern: '**/target/jacoco.exec', // Reads JaCoCo execution file
                           classPattern: '**/target/classes',
                           sourcePattern: '**/src/main/java',
                           exclusionPattern: '**/test/**'
                }
            }
        }
    }
}
