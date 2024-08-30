package org.dynapi.dynapi.web.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.common.utils.SetUtils;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.dynapi.core.query.QueryConfig;
import org.dynapi.dynapi.core.query.QueryConfigParser;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * query endpoint for the data
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class Api {
    private final Query query;
    private final MetaQueryGenerator metaQueries;

    private JdbcTemplate jdbcTemplate;

    /**
     * fetches a list of entries
     */
    @GetMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(HttpServletRequest request, @PathVariable("schema") String schemaName, @PathVariable("table") String tableName) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        final String columnInfoQuery = metaQueries.listColumnsOfTable(schemaName, tableName);
        List<Map<String, Object>> tableColumnsInfos = jdbcTemplate.queryForList(columnInfoQuery);
        List<String> tableColumnNames = tableColumnsInfos.stream().map(info -> (String) info.get("column_name")).toList();

        List<String> queryColumns = List.of(queryConfig.getColumns());
        if (!queryColumns.contains("*")) {
            Set<String> unknownColumns = SetUtils.difference(new HashSet<>(queryColumns), new HashSet<>(tableColumnNames));
            if (!unknownColumns.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown table columns: " + String.join(", ", unknownColumns));
        }

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = query
                .from(table)
                .select((Object[]) queryConfig.getColumns());

        if (queryConfig.getLimit() != null)
            queryBuilder.limit(queryConfig.getLimit());
        if (queryConfig.getOffset() != null)
            queryBuilder.offset(queryConfig.getOffset());

        if (queryConfig.getOrderBy() != null) {
            for (QueryConfig.OrderBy orderBy : queryConfig.getOrderBy())
                queryBuilder.orderBy(new QueryBuilder.OrderByEntry(table.field(orderBy.column()), orderBy.order()));
        }

        String sqlQuery = queryBuilder.getSql();
        log.info(sqlQuery);

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sqlQuery);
        return ResponseEntity.ok(data);
    }

    /**
     * fetches exactly one entry or throws `404 Not Found`
     */
    @GetMapping(value = "/{schema}/{table}/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOne(HttpServletRequest request, @PathVariable String schema, @PathVariable String table) {
        return ResponseEntity.ok().build();
    }

    /**
     * adds one or more entries
     */
    @PostMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> post(HttpServletRequest request, @PathVariable String schema, @PathVariable String table) {
        return ResponseEntity.ok().build();
    }

    /**
     * replaces all matching with the passed entry
     */
    @PutMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> put(HttpServletRequest request, @PathVariable String schema, @PathVariable String table) {
        return ResponseEntity.ok().build();
    }

    /**
     * partially modifies a resource
     */
    @PatchMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patch(HttpServletRequest request, @PathVariable String schema, @PathVariable String table) {
        return ResponseEntity.ok().build();
    }

    /**
     * deletes one or more entries
     */
    @DeleteMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable String schema, @PathVariable String table) {
        return ResponseEntity.ok().build();
    }
}
