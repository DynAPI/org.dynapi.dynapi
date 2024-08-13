package org.dynapi.dynapi.core.config;

import lombok.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.dynapi.jsonschema.gen.annotations.*;
import org.dynapi.jsonschema.gen.JsonSchemaGenerator;

import java.io.*;

@Data
@RequiredArgsConstructor
@Description("DynAPI Configuration")
public class DynAPIConfiguration {
    @Description("enable additional debug information.\nWarning: This may disclose sensible information")
    private boolean debug = false;
    @Hidden
    private boolean developmentDebug = false;
    @Description("Configuration for the HTTP-server")
    private ServerConfiguration server = new ServerConfiguration();
    @Description("Configuration for the features of the webpages")
    private WebConfiguration web = new WebConfiguration();
    @Description("Configuration for the database connection")
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

    public static String getJsonSchemaStr() {
        return JsonSchemaGenerator.generateJsonSchemaAsString(DynAPIConfiguration.class);
    }

    @Data
    @RequiredArgsConstructor
    public static class ServerConfiguration {
        @Description("network address the server should bind to")
        private String host = "0.0.0.0";
        @Description("server port")
        @Constraints(gt = 0)
        private Integer port = 6889;
        @Description("base url of the server")
        @Constraints(pattern = "^/.*")
        private String baseurl = "/";
    }

    @Data
    @RequiredArgsConstructor
    public static class WebConfiguration {
        @Description("whether to make the openapi endpoint available (required for swagger and redoc)")
        private boolean openapi = true;
        @Description("whether to offer swagger as documentation tool")
        private boolean swagger = true;
        @Description("whether to offer redoc as documentation tool")
        private boolean redoc = true;
    }

    @Data
    @RequiredArgsConstructor
    public static class DatabaseConfiguration {
        @Required
        @Description("database dialect")
        @Examples({"clickhouse", "mssql", "mysql", "oracle", "redshift", "postgresql", "snowflake", "sqlite", "vertica"})
        private String dialect;
        @Description("FQDN of the database server")
        private String host = "localhost";
        @Description("port of the database")
        @Constraints(gt = 0)
        private Integer port = null;
        @Description("database to use")
        private String database = null;
        @RequiredIf("password")
        @Description("username of the user to connect with")
        private String username = null;
        @RequiredIf("username")
        @Description("password of the user to connect with")
        private String password = null;
    }
}
