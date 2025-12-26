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
                echo "${params.SERVICE_NAME} 유닛 테스트를 시작합니다... "
                dir("${params.SERVICE_NAME}") {
                    // 1. 루트의 gradlew를 사용해 '선택한 모듈'의 테스트만 돌림
                    sh '../gradlew :${params.SERVICE_NAME}:test --no-daemon'
                }
            }
            post {
                always {
                    // 2. 테스트가 성공하든 실패하든 리포트 남기기
                    // 해당 서비스 폴더 내부의 결과만 수집
                    junit "${params.SERVICE_NAME}/build/test-results/test/*.xml"
                }
            }
        }

        stage('Source Build') {
            steps {
                echo "${params.SERVICE_NAME} 소스 빌드를 시작합니다..."
                dir("${params.SERVICE_NAME}") {
                    // 테스트는 위에서 했으니 여기선 건너뜀
                    sh '../gradlew :${params.SERVICE_NAME}:bootJar --no-daemon -x test'
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