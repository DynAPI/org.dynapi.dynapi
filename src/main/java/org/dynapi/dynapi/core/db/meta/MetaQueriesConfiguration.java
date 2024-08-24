package org.dynapi.dynapi.core.db.meta;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.impl.PostgreSQLMetaQueries;
import org.dynapi.dynapi.core.db.meta.impl.SQLiteMetaQueries;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class MetaQueriesConfiguration {
    /**
     * used by implementations of {@link MetaQueryGenerator} to get registered
     */
    public static final Map<String, Supplier<MetaQueryGenerator>> registeredGenerator = new HashMap<>();

    static {
        registeredGenerator.put("postgresql", PostgreSQLMetaQueries::new);
        registeredGenerator.put("sqlite", SQLiteMetaQueries::new);
    }

    @Bean
    public MetaQueryGenerator metaQueryGenerator() {
        String dialect = System.getProperty("dynapi.dialect");
        Supplier<MetaQueryGenerator> newMetaQueryGenerator = registeredGenerator.getOrDefault(dialect, null);
        if (newMetaQueryGenerator == null)
            throw new RuntimeException("Unknown dialect: " + dialect);
        return newMetaQueryGenerator.get();
    }
}
