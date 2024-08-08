package org.dynapi.dynapi.core.db.meta.impl;

import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.sqlite.SQLiteQuery;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.List;

public class SQLiteMetaQueries implements MetaQueryGenerator {

    @Override
    public String listSchemasQuery() {
        return SQLiteQuery
                .select(List.of(new ValueWrapper("schemaname", "main")))
                .getSql();
    }

    @Override
    public String listTablesQuery() {
        Table table = new Schema("main").getTable("sqlite_master");
        Field typeField = table.field("type");
        return SQLiteQuery
                .from(table)
                .select(new ValueWrapper("schemaname", "main"), new Field("tablename", "name", table))
                .where(typeField.eq("table"))
                .getSql();
    }

    @Override
    public String listTableOfSchemaQuery(String schemaName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String listColumnsOfTableQuery(String schemaName, String tableName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
