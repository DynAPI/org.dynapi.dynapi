package org.dynapi.dynapi.core.database.impl.sqlite;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.AbstractDatabase;
import org.dynapi.dynapi.core.database.interfaces.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.sqlite.SQLiteQuery;
import org.dynapi.squirtle.core.queries.Query;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class SQLiteDatabase extends AbstractDatabase {
    @Getter
    private final MetaQueryGenerator metaQueryGenerator = new SQLiteMetaQueryGenerator();

    public SQLiteDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Query newQuery() {
        return new SQLiteQuery();
    }
}
