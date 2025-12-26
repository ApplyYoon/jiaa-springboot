package io.github.jiwontechinnovation.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    @Primary
    public OpenApiCustomizer serverOpenApiCustomizer() {
        return openApi -> {
            // 모든 서비스의 서버 URL을 localhost로 강제 오버라이드
            Server server = new Server();
            server.setUrl("http://localhost:" + serverPort);
            server.setDescription("Gateway Server (Local Development)");
            
            // 기존 서버 정보를 모두 제거하고 localhost만 설정
            openApi.getServers().clear();
            openApi.setServers(List.of(server));
        };
    }
}

