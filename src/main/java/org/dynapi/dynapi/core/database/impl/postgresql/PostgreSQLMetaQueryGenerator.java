package org.dynapi.dynapi.core.database.impl.postgresql;

import org.dynapi.dynapi.core.database.interfaces.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.postgresql.PostgreSQLQuery;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class PostgreSQLMetaQueryGenerator implements MetaQueryGenerator {
    @Override
    public String listSchemas() {
        Table pgNamespace = new Schema("pg_catalog").table("pg_namespace");
        Field schemaName = pgNamespace.field("nspname").as("schema_name");
        return new PostgreSQLQuery()
            .from(pgNamespace)
            .select(schemaName)
            .where(schemaName.not_like("pg_%"))
            .getSql();
    }

    @Override
    public String listTables() {
        Table pgTables = new Schema("pg_catalog").table("pg_tables");
        Field schemaName = pgTables.field("schemaname").as("schema_name");
        Field tableName = pgTables.field("tablename").as("table_name");
        return new PostgreSQLQuery()
                .from(pgTables)
                .select(schemaName, tableName)
                .where(schemaName.not_like("pg_%"))
                .where(tableName.not_like("pg_%"))
                .getSql();
    }

    @Override
    public String listTablesOfSchema(String ofSchemaName) {
        Table pgTables = new Schema("pg_catalog").table("pg_tables");
        Field schemaName = pgTables.field("schema_name");
        Field tableName = pgTables.field("table_name");
        return new PostgreSQLQuery()
                .from(pgTables)
                .select(schemaName, tableName)
                .where(schemaName.not_like("pg_%"))
                .where(tableName.not_like("pg_%"))
                .where(schemaName.eq(ofSchemaName))
                .getSql();
    }

    @Override
    public String listColumnsOfTable(String schema, String table) {
        Table columns = new Schema("information_schema").table("columns");
        Field tableSchema = columns.field("table_schema");
        Field tableName = columns.field("table_name");
        return new PostgreSQLQuery()
                .from(columns)
                .select(
                        columns.field("column_name").as("column_name"),
                        columns.field("data_type").as("type")
                )
                .where(tableSchema.eq(schema).and(tableName.eq(table)))
                .getSql();
    }
}
