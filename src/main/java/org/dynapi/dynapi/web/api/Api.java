package org.dynapi.dynapi.web.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.common.utils.SetUtils;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.dynapi.core.query.QueryConfig;
import org.dynapi.dynapi.core.query.QueryConfigParser;
import org.dynapi.squirtle.core.PseudoColumns;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

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

        validateColumnAgainstTable(queryConfig.getColumns(), schemaName, tableName);

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

        Stream<Map<String, Object>> entries = jdbcTemplate.queryForStream(sqlQuery, new ColumnMapRowMapper());
        return ResponseEntity.ok(entries);
    }

    /**
     * fetches exactly one entry or throws `404 Not Found`
     */
    @GetMapping(value = "/{schema}/{table}/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOne(HttpServletRequest request, @PathVariable("schema") String schemaName, @PathVariable("table") String tableName) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        validateColumnAgainstTable(queryConfig.getColumns(), schemaName, tableName);

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = query
                .from(table)
                .select((Object[]) queryConfig.getColumns())
                .limit(1);

        if (queryConfig.getOffset() != null)
            queryBuilder.offset(queryConfig.getOffset());

        if (queryConfig.getOrderBy() != null) {
            for (QueryConfig.OrderBy orderBy : queryConfig.getOrderBy())
                queryBuilder.orderBy(new QueryBuilder.OrderByEntry(table.field(orderBy.column()), orderBy.order()));
        }

        String sqlQuery = queryBuilder.getSql();
        log.info(sqlQuery);

        Map<String, Object> entry = jdbcTemplate.queryForObject(sqlQuery, new ColumnMapRowMapper());
        if (entry == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No entry found");
        return ResponseEntity.ok(entry);
    }

    /**
     * fetches exactly one entry by the ROWID or throws `404 Not Found`
     */
    @GetMapping(value = "/{schema}/{table}/{rowid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOne(HttpServletRequest request, @PathVariable("schema") String schemaName, @PathVariable("table") String tableName, @PathVariable("rowid") long rowid) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        validateColumnAgainstTable(queryConfig.getColumns(), schemaName, tableName);

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = query
                .from(table)
                .select((Object[]) queryConfig.getColumns())
                .where(PseudoColumns.RowID.eq(rowid))
                .limit(1);

        String sqlQuery = queryBuilder.getSql();
        log.info(sqlQuery);

        Map<String, Object> entry = jdbcTemplate.queryForObject(sqlQuery, new ColumnMapRowMapper());
        if (entry == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No entry found with ROWID=" + rowid);
        return ResponseEntity.ok(entry);
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

    private void validateColumnAgainstTable(String[] columns, String schemaName, String tableName) {
        final String columnInfoQuery = metaQueries.listColumnsOfTable(schemaName, tableName);
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
