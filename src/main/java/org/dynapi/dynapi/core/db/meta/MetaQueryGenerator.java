package org.dynapi.dynapi.core.db.meta;

public interface MetaQueryGenerator {
    String listSchemasQuery();
    String listTablesQuery();
    String listTableOfSchemaQuery(String schemaName);
    String listColumnsOfTableQuery(String schemaName, String tableName);
}
