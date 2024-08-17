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
    public String listSchemas() {
        return SQLiteQuery
                .select(List.of(new ValueWrapper("schemaname", "main")))
                .getSql();
    }

    @Override
    public String listTables() {
        return listTableOfSchema("main");
    }

    @Override
    public String listTableOfSchema(String schemaName) {
        Table table = new Schema(schemaName).table("sqlite_master");
        Field typeField = table.field("type");
        return SQLiteQuery
                .from(table)
                .select(new ValueWrapper("schemaname", schemaName), new Field("tablename", "name", table))
                .where(typeField.eq("table"))
                .getSql();
    }

    @Override
    public String listColumnsOfTable(String schemaName, String tableName) {
        throw new UnsupportedOperationException("Not implemented yet");
//        return SQLiteQuery
//                .from(new Function(null, "pragma_table_info", tableName))
//                .select(
//                        new Field("column_name", "name"),
//                        new Field("type", "type")
//                )
//                .getSql();
    }
}
