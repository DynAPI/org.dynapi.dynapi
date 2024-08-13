package org.dynapi.dynapi.core.db.meta;

public interface MetaQueryGenerator {
    String listSchemas();
    String listTables();
    String listTableOfSchema(String schemaName);
    String listColumnsOfTable(String schemaName, String tableName);
}
