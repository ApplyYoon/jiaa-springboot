pipeline {
    agent any
    
    parameters {
        choice(name: 'SERVICE_NAME', 
               choices: ['user-service', 'auth-service', 'analysis-service', 'goal-service', 'gateway-service'], 
               description: '빌드할 서비스를 선택하세요')
    }

    stages {
        stage('Unit Test') {
            steps {
                echo "${params.SERVICE_NAME} 유닛 테스트를 시작합니다..."
                dir("${params.SERVICE_NAME}") {
                    sh "chmod +x ../gradlew"
                    sh "../gradlew :${params.SERVICE_NAME}:test --no-daemon"
                }
            }
            post {
                always {
                    junit "${params.SERVICE_NAME}/build/test-results/test/*.xml"
                }
            }
        }

        stage('Source Build') {
            steps {
                echo "${params.SERVICE_NAME} 소스 빌드를 시작합니다..."
                dir("${params.SERVICE_NAME}") {
                    // 테스트는 위에서 했으니 테스트는 건너뜀
                    sh "../gradlew :${params.SERVICE_NAME}:bootJar --no-daemon -x test"
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                dir("${params.SERVICE_NAME}") {
                    echo "이미지 빌드를 시작합니다..."
                    sh "docker build -t jiaa-${params.SERVICE_NAME}:${env.BUILD_NUMBER} ." 
                    sh "docker tag jiaa-${params.SERVICE_NAME}:${env.BUILD_NUMBER} jiaa-${params.SERVICE_NAME}:latest"
                }
            }
        }
    }
}