package org.dynapi.dynapi.core.config;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

@Slf4j
@Data
@RequiredArgsConstructor
public class DynAPIConfiguration {
    private boolean debug = false;
    private boolean developmentDebug = false;
    private ServerConfiguration server = new ServerConfiguration();
    private WebConfiguration web = new WebConfiguration();
    private DatabaseConfiguration database = new DatabaseConfiguration();

    public static DynAPIConfiguration load() {
        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(DynAPIConfiguration.class, loaderOptions));
        File fp = new File(System.getProperty("dynapi.config", "dynapi.yaml")).getAbsoluteFile();
        try (InputStream stream = new FileInputStream(fp)) {
            return yaml.load(stream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Configuration File '%s' does not exist.", fp), e);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error reading configuration file '%s'.", fp), e);
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class ServerConfiguration {
        private String host = "0.0.0.0";
        private Integer port = 6889;
        private String baseurl = "/";
    }

    @Data
    @RequiredArgsConstructor
    public static class WebConfiguration {
        private boolean openapi = true;
        private boolean swagger = true;
        private boolean redoc = true;
    }

    @Data
    @RequiredArgsConstructor
    public static class DatabaseConfiguration {
        private String dialect;
        private String host = "localhost";
        private Integer port = null;
        private String database = null;
        private String username = null;
        private String password = null;
    }
}
