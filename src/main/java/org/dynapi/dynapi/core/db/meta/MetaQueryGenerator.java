package org.dynapi.dynapi.core.db.meta;

public interface MetaQueryGenerator {
    /**
     * returns an SQL-Query that returns the available database schemas <br>
     * format: {@code {'schemaname'}}
     */
    String listSchemas();

    /**
     * returns an SQL-Query that returns the available tables with their schema <br>
     * format: {@code {'schemaname', 'tablename'}}
     */
    String listTables();

    /**
     * returns an SQL-Query that returns the available tables of a specific schema <br>
     * format: {@code {'schemaname', 'tablename'}}
     * @param schemaName database schema
     */
    String listTableOfSchema(String schemaName);

    /**
     * returns an SQL-Query that returns the column-information of a specific table of a specific schema <br>
     * format: {@code {'column_name', 'type'}}
     * @param schemaName database schema
     * @param tableName table of {@code schemaName}
     */
    String listColumnsOfTable(String schemaName, String tableName);
}
