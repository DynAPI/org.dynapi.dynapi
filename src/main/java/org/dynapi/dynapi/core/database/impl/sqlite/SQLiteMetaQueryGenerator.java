package org.dynapi.dynapi.core.database.impl.sqlite;

import org.dynapi.dynapi.core.database.interfaces.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.sqlite.SQLiteQuery;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Selectable;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.functions.TableValuedFunction;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SQLiteMetaQueryGenerator implements MetaQueryGenerator {

    @Override
    public String listSchemas() {
        // even if the pragma is called "database_list" it basically the schemas
        Selectable pragma = new TableValuedFunction("pragma_database_list");
        return new SQLiteQuery()
                .from(pragma)
                .select(
                        pragma.field("name").as("schema_name")
                )
                .getSql();
    }

    @Override
    public String listTables() {
        Selectable table_list = new TableValuedFunction("pragma_table_list").as("table_list");
        return new SQLiteQuery()
                .from(table_list)
                .select(
                        table_list.field("schema").as("schema_name"),
                        table_list.field("name").as("table_name")
                )
                .where(table_list.field("name").not_like("sqlite_%"))
                .getSql();
    }

    @Override
    public String listTablesOfSchema(String schemaName) {
        Table table = new Schema(schemaName).table("sqlite_master");
        return new SQLiteQuery()
                .from(table)
                .select(
                        new ValueWrapper(schemaName).as("schema_name"),
                        new Field("name", table).as("table_name")
                )
                .where(table.field("type").eq("table"))
                .getSql();
    }

    @Override
    public String listColumnsOfTable(String schemaName, String tableName) {
        // todo: $schema.pragme_table_info($tableName)
        Selectable tableInfo = new TableValuedFunction("pragma_table_info", tableName);
        return new SQLiteQuery()
                .from(tableInfo)
                .select(
                        tableInfo.field("name").as("column_name"),
                        tableInfo.field("type").as("type")
                )
                .getSql();
    }
}
