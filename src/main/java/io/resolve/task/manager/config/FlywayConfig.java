package io.resolve.task.manager.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    Flyway loadFlyway() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:mem:resolvedb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "sa", null)
                .load();

        flyway.migrate();

        return flyway;
    }


}
