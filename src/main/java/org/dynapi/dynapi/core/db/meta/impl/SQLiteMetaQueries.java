package org.dynapi.dynapi.core.db.meta.impl;

import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.squirtle.core.dialects.sqlite.SQLiteQuery;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.functions.TableValuedFunction;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.List;

public class SQLiteMetaQueries implements MetaQueryGenerator {

    @Override
    public String listSchemas() {
        return new SQLiteQuery()
                .select(List.of(new ValueWrapper("main").as("schemaname")))
                .getSql();
    }

    @Override
    public String listTables() {
        return listTablesOfSchema("main");
    }

    @Override
    public String listTablesOfSchema(String schemaName) {
        Table table = new Schema(schemaName).table("sqlite_master");
        Field typeField = table.field("type");
        return new SQLiteQuery()
                .from(table)
                .select(new ValueWrapper(schemaName).as("schemaname"), new Field("name", table).as("tablename"))
                .where(typeField.eq("table"))
                .getSql();
    }

    @Override
    public String listColumnsOfTable(String schemaName, String tableName) {
        return new SQLiteQuery()
                .from(new TableValuedFunction("pragma_table_info", tableName))
                .select(
                        new Field("name").as("column_name"),
                        new Field("type").as("type")
                )
                .getSql();
    }
}
