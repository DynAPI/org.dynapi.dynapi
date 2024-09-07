package org.dynapi.dynapi.web.api.delete;

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
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class DeleteController {
    private final Database database;
    private final JdbcTemplate jdbcTemplate;

    /**
     * deletes one or more entries
     */
    @DeleteMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMultiple(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName
    ) {
        QueryConfig queryConfig = QueryConfigParser.parse(request);

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = database.newQuery()
                .from(table)
                .delete();

        // todo: where

        if (queryConfig.getLimit() != null)
            queryBuilder.limit(queryConfig.getLimit());

        if (queryConfig.getOffset() != null)
            queryBuilder.offset(queryConfig.getOffset());

        if (queryConfig.getOrderBy() != null) {
            for (QueryConfig.OrderBy orderBy : queryConfig.getOrderBy())
                queryBuilder.orderBy(new QueryBuilder.OrderByEntry(table.field(orderBy.column()), orderBy.order()));
        }

        String sql = queryBuilder.getSql();
        log.info(sql);

        int affected = jdbcTemplate.update(sql);
        String responseContent = new JSONObject()
                .put("affected", affected)
                .toString();
        return ResponseEntity.ok(responseContent);
    }

    /**
     * deletes one or more entries
     */
    @DeleteMapping(value = "/{schema}/{table}/{rowid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteByRowId(
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName,
            @PathVariable("rowid") long rowid
    ) {
        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        QueryBuilder queryBuilder = database.newQuery()
                .from(table)
                .delete()
                .where(PseudoColumns.RowID.eq(rowid))
                .limit(1);

        String sqlQuery = queryBuilder.getSql();
        log.info(sqlQuery);

        jdbcTemplate.execute(sqlQuery);
        return ResponseEntity.noContent().build();
    }
}
