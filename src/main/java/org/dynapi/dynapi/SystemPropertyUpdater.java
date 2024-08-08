package org.dynapi.dynapi;

import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemPropertyUpdater {
    private static final Logger log = LoggerFactory.getLogger(SystemPropertyUpdater.class);

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
