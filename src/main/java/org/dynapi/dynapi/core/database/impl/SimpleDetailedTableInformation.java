package org.dynapi.dynapi.core.database.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.dynapi.dynapi.core.database.interfaces.ColumnInformation;
import org.dynapi.dynapi.core.database.interfaces.DetailedTableInformation;

import java.util.List;

/**
 * simple implementation of {@link DetailedTableInformation}
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SimpleDetailedTableInformation extends SimpleTableInformation implements DetailedTableInformation {
    @Getter
    private final List<ColumnInformation> columnInformation;
    @Getter(lazy = true)
    private final List<String> columnNames = columnInformation.stream().map(ColumnInformation::getColumnName).toList();

    public SimpleDetailedTableInformation(String schemaName, String tableName, List<ColumnInformation> columnInformation) {
        super(schemaName, tableName);
        this.columnInformation = columnInformation;
    }
}
