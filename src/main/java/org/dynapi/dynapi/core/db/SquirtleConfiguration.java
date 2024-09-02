package org.dynapi.dynapi.core.db;

import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.dynapi.core.db.meta.impl.PostgreSQLMetaQueryGenerator;
import org.dynapi.dynapi.core.db.meta.impl.SQLiteMetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.clickhouse.ClickHouseQuery;
import org.dynapi.squirtle.core.dialects.mssql.MSSQLQuery;
import org.dynapi.squirtle.core.dialects.mysql.MySQLQuery;
import org.dynapi.squirtle.core.dialects.oracle.OracleQuery;
import org.dynapi.squirtle.core.dialects.postgresql.PostgreSQLQuery;
import org.dynapi.squirtle.core.dialects.redshift.RedshiftQuery;
import org.dynapi.squirtle.core.dialects.snowflake.SnowflakeQuery;
import org.dynapi.squirtle.core.dialects.sqlite.SQLiteQuery;
import org.dynapi.squirtle.core.queries.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class SquirtleConfiguration {
    /**
     * used by implementations of {@link MetaQueryGenerator} to get registered
     */
    public static final Map<String, Supplier<MetaQueryGenerator>> registeredMetaGenerators = new HashMap<>();

    static {
        registeredMetaGenerators.put("postgresql", PostgreSQLMetaQueryGenerator::new);
        registeredMetaGenerators.put("sqlite", SQLiteMetaQueryGenerator::new);
    }

    public static final Map<String, Supplier<Query>> registeredQueryClasses = new HashMap<>();

    static {
        registeredQueryClasses.put("clickhouse", ClickHouseQuery::new);
        registeredQueryClasses.put("mssql", MSSQLQuery::new);
        registeredQueryClasses.put("mysql", MySQLQuery::new);
        registeredQueryClasses.put("oracle", OracleQuery::new);
        registeredQueryClasses.put("postgresql", PostgreSQLQuery::new);
        registeredQueryClasses.put("redshift", RedshiftQuery::new);
        registeredQueryClasses.put("snowflake", SnowflakeQuery::new);
        registeredQueryClasses.put("sqlite", SQLiteQuery::new);
    }

    @Bean
    public MetaQueryGenerator metaQueryGenerator() {
        String dialect = System.getProperty("dynapi.dialect");
        Supplier<MetaQueryGenerator> newMetaQueryGenerator = registeredMetaGenerators.getOrDefault(dialect, null);
        if (newMetaQueryGenerator == null)
            throw new RuntimeException("Unknown dialect: " + dialect);
        return newMetaQueryGenerator.get();
    }

    @Bean
    public Query query() {
        String dialect = System.getProperty("dynapi.dialect");
        Supplier<Query> queryClassGenerator = registeredQueryClasses.getOrDefault(dialect, null);
        if (queryClassGenerator == null)
            throw new RuntimeException("Unknown dialect: " + dialect);
        return queryClassGenerator.get();

    }
}
