package org.dynapi.dynapi;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@SpringBootApplication
public class Application {
    // somehow we have to create an instance here. otherwise it won't build
    private static DynAPIConfiguration configuration = new DynAPIConfiguration();

    public static void main(String[] args) {
        List<String> argList = List.of(args);
        if (argList.contains("-h") || argList.contains("--help")) {
            System.out.println("dynapi [-h] [-v] [--json-schema]");
            return;
        } else if (argList.contains("--json-schema")) {
            System.out.println(DynAPIConfiguration.getJsonSchemaStr());
            return;
        }
        Application.configuration = DynAPIConfiguration.load();
        SystemPropertyUpdater.updateProperties(configuration);
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DynAPIConfiguration dynAPIConfiguration() {
        return configuration;
    }
}
