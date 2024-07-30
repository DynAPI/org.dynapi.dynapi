package org.dynapi.dynapi;

import org.dynapi.dynapi.core.config.DynAPIConfiguration;

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
        StringBuilder dataSource = new StringBuilder("jdbc:");
        dataSource.append(configuration.getDialect()).append("://");
        dataSource.append(configuration.getHost());
        if (configuration.getPort() != null)
            dataSource.append(":").append(configuration.getPort());
        if (configuration.getDatabase() != null)
            dataSource.append("/").append(configuration.getDatabase());
        System.setProperty("spring.datasource.url", dataSource.toString());

        if (configuration.getUsername() != null)
            System.setProperty("spring.datasource.username", configuration.getUsername());

        if (configuration.getPassword() != null)
            System.setProperty("spring.datasource.password", configuration.getPassword());
    }
}
