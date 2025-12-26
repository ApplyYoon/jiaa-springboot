# Amazon Linux 2023 기반의 Corretto 21 JRE 버전
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-jre

WORKDIR /app

# 보안 업데이트
RUN dnf update -y && dnf clean all

# 빌드된 JAR 복사 (기존과 동일)
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]