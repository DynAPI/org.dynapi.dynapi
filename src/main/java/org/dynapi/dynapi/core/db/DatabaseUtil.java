package org.dynapi.dynapi.core.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.common.utils.SetUtils;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseUtil {
    private final MetaQueryGenerator metaQueries;

    private JdbcTemplate jdbcTemplate;

    public void validateColumnAgainstTable(String[] columns, String schemaName, String tableName) {
        final String columnInfoQuery = metaQueries.listColumnsOfTable(schemaName, tableName);
        log.info(columnInfoQuery);
        List<Map<String, Object>> tableColumnsInfos = jdbcTemplate.queryForList(columnInfoQuery);
        List<String> tableColumnNames = tableColumnsInfos.stream().map(info -> (String) info.get("column_name")).toList();

        List<String> queryColumns = List.of(columns);
        if (!queryColumns.contains("*")) {
            Set<String> unknownColumns = SetUtils.difference(new HashSet<>(queryColumns), new HashSet<>(tableColumnNames));
            if (!unknownColumns.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown table columns: " + String.join(", ", unknownColumns));
        }
    }
}
