package org.dynapi.dynapi.web.api.get;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.interfaces.Database;
import org.dynapi.dynapi.core.query.QueryConfig;
import org.dynapi.dynapi.core.query.QueryConfigParser;
import org.dynapi.squirtle.core.PseudoColumns;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class GetController {
    private final Database database;

    private JdbcTemplate jdbcTemplate;

    /**
     * fetches a list of entries
     */
    @GetMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMultiple(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName
    ) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        database.validateThatTableHasColumns(schemaName, tableName, queryConfig.getColumns());

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = database.newQuery()
                .from(table)
                .select((Object[]) queryConfig.getColumns());

        if (queryConfig.getWheres() != null) {
            for (QueryConfig.Where where : queryConfig.getWheres())
                queryBuilder.where(table.field(where.column()).eq(where.value()));
        }

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
    public ResponseEntity<?> getOne(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName
    ) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        database.validateThatTableHasColumns(schemaName, tableName, queryConfig.getColumns());

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = database.newQuery()
                .from(table)
                .select((Object[]) queryConfig.getColumns())
                .limit(1);

        if (queryConfig.getWheres() != null) {
            for (QueryConfig.Where where : queryConfig.getWheres())
                queryBuilder.where(table.field(where.column()).eq(where.value()));
        }

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
    public ResponseEntity<?> getOneByRowId(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName,
            @PathVariable("rowid") long rowid
    ) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        database.validateThatTableHasColumns(schemaName, tableName, queryConfig.getColumns());

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = database.newQuery()
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
}
