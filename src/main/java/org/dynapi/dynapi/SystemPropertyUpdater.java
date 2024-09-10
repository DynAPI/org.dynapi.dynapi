package org.dynapi.dynapi;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;

@Slf4j
public class SystemPropertyUpdater {
    public static void updateProperties(DynAPIConfiguration configuration) {
        if ((configuration.isDebug()))
            System.setProperty("spring.profiles.active", "dev");

        updateServer(configuration.getServer());
        updateDatabase(configuration.getDatabase());
        updateApi(configuration.getApi());
        updateLogging(configuration.getLogging());
    }

    private static void updateServer(DynAPIConfiguration.ServerConfiguration configuration) {
        if (configuration.getHost() != null)
            System.setProperty("server.address", configuration.getHost());

        if (configuration.getPort() != null)
            System.setProperty("server.port", String.valueOf(configuration.getPort()));

        if (configuration.getBaseurl() != null)
            System.setProperty("server.servlet.context-path", configuration.getBaseurl());

        if (configuration.getCompression() != null)
            updateServerCompression(configuration.getCompression());
    }

    private static void updateServerCompression(DynAPIConfiguration.ServerConfiguration.CompressionConfiguration configuration) {
        if (configuration.isEnabled())
            System.setProperty("server.compression.enabled", String.valueOf(true));

        if (configuration.getMinResponseSize() != null)
            System.setProperty("server.compression.min-response-size", configuration.getMinResponseSize());
    }

    private static void updateApi(DynAPIConfiguration.ApiConfiguration configuration) {
        if (configuration.getStreamingResponseTimeout() != null)
            System.setProperty("spring.mvc.async.request-timeout", String.valueOf(configuration.getStreamingResponseTimeout()));
    }

    private static void updateDatabase(DynAPIConfiguration.DatabaseConfiguration configuration) {
        String url = configuration.getUrl();
        System.setProperty("spring.datasource.url", url);

        String dialect = configuration.getDialect();
        if (dialect == null) {  // auto-detection of dialect base on the url
            // 'jdbc:{dialect}:{...}'
            int firstColon = url.indexOf(':');
            int secondColon = url.indexOf(':', firstColon + 1);
            dialect = url.substring(firstColon + 1, secondColon);
        }
        System.setProperty("dynapi.dialect", dialect);

        if (configuration.getUsername() != null)
            System.setProperty("spring.datasource.username", configuration.getUsername());

        if (configuration.getPassword() != null)
            System.setProperty("spring.datasource.password", configuration.getPassword());
    }

    private static void updateLogging(DynAPIConfiguration.LoggingConfiguration configuration) {
        if (configuration.getLevel() != null)
            System.setProperty("logging.level.org.dynapi", configuration.getLevel());
    }
}
