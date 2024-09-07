package org.dynapi.dynapi.core.database;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.impl.postgresql.PostgreSQLDatabase;
import org.dynapi.dynapi.core.database.impl.sqlite.SQLiteDatabase;
import org.dynapi.dynapi.core.database.interfaces.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
public class SquirtleConfiguration {
    /**
     * used by implementations of {@link Database} to get registered <br>
     * map key is the dialect
     */
    public static final Map<String, Function<JdbcTemplate, Database>> registeredDatabases = new HashMap<>();

    @Bean
    public Database database(JdbcTemplate jdbcTemplate) {
        String dialect = System.getProperty("dynapi.dialect");
        Function<JdbcTemplate, Database> databaseConstructor = registeredDatabases.getOrDefault(dialect, null);
        if (databaseConstructor == null)
            throw new RuntimeException("Unknown dialect: " + dialect);
        return databaseConstructor.apply(jdbcTemplate);
    }

    static {
        registeredDatabases.put("postgresql", PostgreSQLDatabase::new);
        registeredDatabases.put("sqlite", SQLiteDatabase::new);
    }
}
