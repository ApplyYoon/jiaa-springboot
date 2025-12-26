package io.github.jiwontechinnovation.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
    basePackages = {"io.github.jiwontechinnovation.auth", "io.github.jiwontechinnovation.common", "io.github.jiwontechinnovation.user.entity", "io.github.jiwontechinnovation.user.repository"},
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "io\\.github\\.jiwontechinnovation\\.user\\.config\\..*")
)
@EntityScan(basePackages = {"io.github.jiwontechinnovation.auth.entity", "io.github.jiwontechinnovation.user.entity"})
@EnableJpaRepositories(basePackages = {"io.github.jiwontechinnovation.auth.repository", "io.github.jiwontechinnovation.user.repository"})
class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
