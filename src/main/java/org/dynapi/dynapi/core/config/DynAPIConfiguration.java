package org.dynapi.dynapi.core.config;

import lombok.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.dynapi.jsonschemagen.*;

import java.io.*;

@Data
@RequiredArgsConstructor
@JsonSchemaAble
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
        @Description("database url")
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
        @RequiredIf("password")
        @Description("username of the user to connect with")
        private String username = null;
        @RequiredIf("username")
        @Description("password of the user to connect with")
        private String password = null;
    }
}
