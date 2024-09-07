package org.dynapi.dynapi.core.database;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.common.utils.SetUtils;
import org.dynapi.dynapi.core.database.impl.SimpleColumnInformation;
import org.dynapi.dynapi.core.database.impl.SimpleTableInformation;
import org.dynapi.dynapi.core.database.interfaces.ColumnInformation;
import org.dynapi.dynapi.core.database.interfaces.Database;
import org.dynapi.dynapi.core.database.interfaces.TableInformation;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * basic implementation of {@link Database} which speeds up development
 */
@Slf4j
abstract public class AbstractDatabase implements Database {
    protected final JdbcTemplate jdbcTemplate;

    public AbstractDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getSchemaNames() {
        String sql = getMetaQueryGenerator().listSchemas();
        log.debug(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result.stream().map(e -> (String) e.get("schema_name")).toList();
    }

    @Override
    public List<String> getTableNamesOfSchema(@NonNull String schemaName) {
        String sql = getMetaQueryGenerator().listTablesOfSchema(schemaName);
        log.debug(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result.stream().map(e -> (String) e.get("table_name")).toList();
    }

    @Override
    public List<TableInformation> getTableInformation() {
        String sql = getMetaQueryGenerator().listTables();
        log.debug(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result
                .stream()
                .map(e -> (TableInformation) new SimpleTableInformation(
                        (String) e.get("schema_name"),
                        (String) e.get("table_name")
                ))
                .toList();
    }

    @Override
    public List<TableInformation> getTableInformationOfSchema(@NonNull String schemaName) {
        String sql = getMetaQueryGenerator().listTablesOfSchema(schemaName);
        log.debug(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result
                .stream()
                .map(e -> (TableInformation) new SimpleTableInformation(
                        (String) e.get("schema_name"),
                        (String) e.get("table_name")
                ))
                .toList();
    }

    @Override
    public List<ColumnInformation> getColumnInformationOfTable(@NonNull String schemaName, @NonNull String tableName) {
        String sql = getMetaQueryGenerator().listColumnsOfTable(schemaName, tableName);
        log.debug(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result
                .stream()
                .map(e -> (ColumnInformation) new SimpleColumnInformation(
                        (String) e.get("column_name"),
                        (String) e.get("type")
                ))
                .toList();
    }

    @Override
    public boolean existsSchema(@NonNull String schemaName) {
        return getSchemaNames().contains(schemaName);
    }

    @Override
    public boolean existsTable(@NonNull String schemaName, @NonNull String tableName) {
        return getTableNamesOfSchema(schemaName).contains(tableName);
    }

    private Set<String> getUnknownColumnsOfTable(@NonNull String schemaName, @NonNull String tableName, @NonNull List<String> columnNames) {
        final String columnInfoQuery = getMetaQueryGenerator().listColumnsOfTable(schemaName, tableName);
        log.info(columnInfoQuery);
        List<Map<String, Object>> tableColumnsInfos = jdbcTemplate.queryForList(columnInfoQuery);
        List<String> tableColumnNames = tableColumnsInfos
                .stream()
                .map(info -> (String) info.get("column_name"))
                .toList();

        return SetUtils.difference(new HashSet<>(columnNames), new HashSet<>(tableColumnNames));
    }

    @Override
    public boolean tableHasAllColumns(@NonNull String schemaName, @NonNull String tableName, @NonNull String[] columnNames) {
        List<String> queryColumns = List.of(columnNames);
        return queryColumns.contains("*") || getUnknownColumnsOfTable(schemaName, tableName, queryColumns).isEmpty();
    }

    @Override
    public void validateThatTableHasColumns(@NonNull String schemaName, @NonNull String tableName, String[] columnNames) {
        List<String> queryColumns = List.of(columnNames);
        if (queryColumns.contains("*")) return;

        Set<String> unknownColumns = getUnknownColumnsOfTable(schemaName, tableName, queryColumns);
        if (!unknownColumns.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown table columns: " + String.join(", ", unknownColumns));
    }
}
