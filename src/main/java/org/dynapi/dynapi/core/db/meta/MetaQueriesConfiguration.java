package org.dynapi.dynapi.core.db.meta;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.impl.PostgreSQLMetaQueries;
import org.dynapi.dynapi.core.db.meta.impl.SQLiteMetaQueries;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MetaQueriesConfiguration {
    public static final Map<String, MetaQueryGenerator> registeredGenerator = new HashMap<>();

    static {
        registeredGenerator.put("postgresql", new PostgreSQLMetaQueries());
        registeredGenerator.put("sqlite", new SQLiteMetaQueries());
    }

    @Bean
    public MetaQueryGenerator metaQueryGenerator() {
        String dialect = System.getProperty("dynapi.dialect");
        MetaQueryGenerator metaQueryGenerator = registeredGenerator.getOrDefault(dialect, null);
        if (metaQueryGenerator == null)
            throw new RuntimeException("Unknown dialect: " + dialect);
        return metaQueryGenerator;
    }
}
