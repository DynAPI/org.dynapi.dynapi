package org.dynapi.dynapi.core.db.meta.impl;

import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.postgresql.PostgreSQLQuery;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class PostgreSQLMetaQueries implements MetaQueryGenerator {
    @Override
    public String listSchemasQuery() {
        return PostgreSQLQuery
            .from(new Schema("pg_catalog").getTable("pg_namespace"))
            .select(new Field("schemaname", "nspname"))
            .getSql();
    }

    @Override
    public String listTablesQuery() {
        return PostgreSQLQuery
                .from(new Schema("pg_catalog").getTable("pg_tables"))
                .select("schemaname", "tablename")
                .getSql();
    }

    @Override
    public String listTableOfSchemaQuery(String schemaName) {
        Table table = new Schema("pg_catalog").getTable("pg_tables");
        Field schemaNameField = table.field("schemaname");
        return PostgreSQLQuery
                .from(table)
                .select(schemaNameField, "tablename")
                .where(schemaNameField.eq(schemaName))
                .getSql();
    }

    @Override
    public String listColumnsOfTableQuery(String schemaName, String tableName) {
        throw new RuntimeException("Not implemented yet");
    }
}
