package org.dynapi.dynapi;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@SpringBootApplication
public class Application {
    public static DynAPIConfiguration configuration = DynAPIConfiguration.load();

    public static void main(String[] args) {
        SystemPropertyUpdater.updateProperties(configuration);
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DynAPIConfiguration dynAPIConfiguration() {
        return configuration;
    }
}
