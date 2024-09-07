package org.dynapi.dynapi.core.database.impl.postgresql;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.AbstractDatabase;
import org.dynapi.dynapi.core.database.interfaces.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.postgresql.PostgreSQLQuery;
import org.dynapi.squirtle.core.queries.Query;
import org.springframework.jdbc.core.JdbcTemplate;


@Slf4j
public class PostgreSQLDatabase extends AbstractDatabase {
    @Getter
    private final MetaQueryGenerator metaQueryGenerator = new PostgreSQLMetaQueryGenerator();

    public PostgreSQLDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Query newQuery() {
        return new PostgreSQLQuery();
    }
}
