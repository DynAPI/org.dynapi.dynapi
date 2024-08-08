package org.dynapi.dynapi;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;

@Slf4j
public class SystemPropertyUpdater {
    public static void updateProperties(DynAPIConfiguration configuration) {
        if ((configuration.isDevelopmentDebug())) {
            System.setProperty("debug", "true");
            System.setProperty("logging.level.root", "debug");
        }

        DynAPIConfiguration.ServerConfiguration serverConf = configuration.getServer();
        if (serverConf != null) updateServer(serverConf);

        DynAPIConfiguration.DatabaseConfiguration databaseConf = configuration.getDatabase();
        if (databaseConf != null) updateDatabase(databaseConf);
    }

    private static void updateServer(DynAPIConfiguration.ServerConfiguration configuration) {
        if (configuration.getHost() != null)
            System.setProperty("server.address", configuration.getHost());

        if (configuration.getPort() != null)
            System.setProperty("server.port", String.valueOf(configuration.getPort()));

        if (configuration.getBaseurl() != null)
            System.setProperty("server.servlet.context-path", configuration.getBaseurl());
    }

    private static void updateDatabase(DynAPIConfiguration.DatabaseConfiguration configuration) {
        String url = configuration.getUrl();
        System.setProperty("spring.datasource.url", url);

        // 'jdbc:{dialect}:{...}'
        int firstColon = url.indexOf(':');
        int secondColon = url.indexOf(':', firstColon + 1);
        String dialect = url.substring(firstColon + 1, secondColon);
        System.setProperty("dynapi.dialect", dialect);

        if (configuration.getUsername() != null)
            System.setProperty("spring.datasource.username", configuration.getUsername());

        if (configuration.getPassword() != null)
            System.setProperty("spring.datasource.password", configuration.getPassword());
    }
}
