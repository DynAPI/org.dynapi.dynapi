package org.dynapi.dynapi.core.database.interfaces;

import lombok.NonNull;
import org.dynapi.squirtle.core.queries.Query;

import java.util.List;

public interface Database {
    /**
     * @return a dialect appropriate {@link Query} to build a sql-query
     */
    Query newQuery();

    /**
     * <b>note:</b> it's recommended to use the getter instead
     * @return a dialect appropriate {@link MetaQueryGenerator} used to fetch table-information
     * @see #getSchemaNames()
     * @see #getTableNamesOfSchema(String)
     * @see #getTableInformation()
     * @see #getTableInformationOfSchema(String)
     */
    MetaQueryGenerator getMetaQueryGenerator();

    // -------------------------------------------------------------------------

    /**
     * @return the names of the schemas
     */
    List<String> getSchemaNames();

    /**
     * @param schemaName schema to filter for
     * @return the names of the tables of a schema
     */
    List<String> getTableNamesOfSchema(@NonNull String schemaName);

    /**
     * @return information about all tables
     */
    List<TableInformation> getTableInformation();

    /**
     * @param schemaName schema to filter for
     * @return information about all tables of a schema
     */
    List<TableInformation> getTableInformationOfSchema(@NonNull String schemaName);

    /**
     * @param schemaName schema of the table
     * @param tableName table
     * @return column-information of the table
     */
    List<ColumnInformation> getColumnInformationOfTable(@NonNull String schemaName, @NonNull String tableName);

    // -------------------------------------------------------------------------

    /**
     * @param schemaName name of the schema to check
     * @return that schema exists
     */
    boolean existsSchema(@NonNull String schemaName);

    /**
     * @param schemaName name of the schema of the table
     * @param tableName name of the table
     * @return that the table exists
     */
    boolean existsTable(@NonNull String schemaName, @NonNull String tableName);

    /**
     * @param schemaName name of the schema of the table
     * @param tableName name of the table
     * @param columnNames columns to verify
     * @return that all specified columns exist on the table
     */
    boolean tableHasAllColumns(@NonNull String schemaName, @NonNull String tableName, @NonNull String[] columnNames);

    /**
     * Utility-function used to validate that a table has all {@code columns} <br>
     * If {@code columns} contains {@code *} then it's directly marked as valid
     * @param schemaName schema-name
     * @param tableName table-name
     * @param columnNames columns to check again
     * @throws org.springframework.web.server.ResponseStatusException if one or more columns are not existing
     */
    void validateThatTableHasColumns(@NonNull String schemaName, @NonNull String tableName, String[] columnNames);
}
