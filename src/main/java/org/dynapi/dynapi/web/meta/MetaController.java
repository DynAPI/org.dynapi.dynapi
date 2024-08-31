package org.dynapi.dynapi.web.meta;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * meta information about the database.
 * like available databases, schemas, tables and columns
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/meta")
public class MetaController {
    private JdbcTemplate database;
    private MetaQueryGenerator queries;

    @GetMapping(value = "/schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> schemas() {
        String sqlQuery = queries.listSchemas();
        log.info(sqlQuery);
        List<Map<String, Object>> raw = database.queryForList(sqlQuery);
        List<String> data = raw.stream().map(entry -> (String) entry.get("schemaname")).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/tables", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tables() {
        String sqlQuery = queries.listTables();
        log.info(sqlQuery);
        List<Map<String, Object>> data = database.queryForList(sqlQuery);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/tables/{schema-name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tablesOfSchema(@PathVariable("schema-name") String schemaName) {
        String sqlQuery = queries.listTablesOfSchema(schemaName);
        log.info(sqlQuery);
        List<Map<String, Object>> data = database.queryForList(sqlQuery);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/columns/{schema-name}/{table-name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> columns(@PathVariable("schema-name") String schemaName, @PathVariable("table-name") String tableName) {
        String sqlQuery = queries.listColumnsOfTable(schemaName, tableName);
        log.info(sqlQuery);
        List<Map<String, Object>> data = database.queryForList(sqlQuery);
        return ResponseEntity.ok(data);
    }
}
