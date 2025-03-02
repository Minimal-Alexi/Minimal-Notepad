pipeline {
    agent any
    stages {
        stage('Create .env File') {
            steps {
                script {
                    writeFile file: '.env', text: """
                    DB_PASSWORD=${env.DB_PASSWORD}
                    SECRET_KEY=${env.SECRET_KEY}
                    """
                }
            }
        }
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Minimal-Alexi/Minimal-Notepad.git'
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
