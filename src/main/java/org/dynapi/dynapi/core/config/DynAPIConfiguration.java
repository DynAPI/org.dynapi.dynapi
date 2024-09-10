package org.dynapi.dynapi.core.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dynapi.jsonschema.gen.JsonSchemaGenerator;
import org.dynapi.jsonschema.gen.annotations.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

@Data
@RequiredArgsConstructor
@Description("DynAPI Configuration")
public class DynAPIConfiguration {
    @Description("Enable additional debug information.\nWarning: This may disclose sensible information")
    private boolean debug = false;
    @Description("Configuration for the HTTP-server")
    private ServerConfiguration server = new ServerConfiguration();
    @Description("Configuration for the features of the webpages")
    private WebConfiguration web = new WebConfiguration();
    @Description("Everything related to how he api endpoints should behave")
    private ApiConfiguration api = new ApiConfiguration();
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
        @Description("Network address the server should bind to")
        private String host = "0.0.0.0";
        @Description("Server port")
        @Constraints(gt = 0)
        private Integer port = 6889;
        @Description("Base-URL of the server")
        @Constraints(pattern = "^/.*")
        private String baseurl = "/";
        @Description("Controls response compression to improve transfer speed and bandwidth utilization")
        private CompressionConfiguration compression = null;

        @Data
        @RequiredArgsConstructor
        public static class CompressionConfiguration {
            @Description("Whether content should get compressed")
            private boolean enabled = true;
            @Description("Minimum response size to send compressed")
            @Constraints(pattern = "(?i)^\\d+(?:b|kb|mb|gb|tb)$")
            private String minResponseSize = null;
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class ApiConfiguration {
        @Description("(possibly) large response are streamed to the client to reduce memory usage on the server." +
                "If the request is enormous or the connection is slow it will be canceled. " +
                "This option configures the maximum timeout for such requests.")
        private Integer streamingResponseTimeout = null;
    }

    @Data
    @RequiredArgsConstructor
    public static class WebConfiguration {
        @Description("Whether to make the openapi endpoint available (required for swagger and redoc)")
        private boolean openapi = true;
        @Description("Whether to offer swagger as a documentation tool")
        private boolean swagger = true;
        @Description("Whether to offer redoc as a documentation tool")
        private boolean redoc = true;
    }

    @Data
    @RequiredArgsConstructor
    public static class DatabaseConfiguration {
        @Required
        @Description("Database URL")
        @Examples({
                "jbdc:clickhouse://localhost:8123",
                "jdbc:sqlserver://localhost:1433",
                "jdbc:mysql://localhost:3306",
                "jdbc:oracle:thin:@localhost:1521/dynapi",
                "jdbc:redshift://examplecluster.abc123xyz789.us-west-2:5439/dynapi",
                "jdbc:postgresql://localhost:5432/dynapi",
                "jdbc:snowflake://dynapi.snowflakecomputing.com",
                "jdbc:sqlite:/dynapi/database.sqlite",
                "jdbc:vertica://localhost:5433/dynapi",
        })
        private String url;
        @Description("SQL-Dialect. This is auto-detected from `url`. If your database is not registered, but uses a known dialect, you can set that here for compatibility.")
        @Examples({"clickhouse", "sqlserver", "mysql", "oracle", "redshift", "postgresql", "snowflake", "sqlite", "vertica"})
        private String dialect = null;
        @RequiredIf("password")
        @Description("Username of the user to connect with")
        private String username = null;
        @RequiredIf("username")
        @Description("Password of the user to connect with")
        private String password = null;
    }
}
