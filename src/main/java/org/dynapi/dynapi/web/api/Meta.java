package org.dynapi.dynapi.web.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class Meta {
    private JdbcTemplate database;
    private MetaQueryGenerator queries;

    @GetMapping(value = "/schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> schemas() {
        String sqlQuery = queries.listSchemasQuery();
        List<Map<String, Object>> raw = database.queryForList(sqlQuery);
        List<String> data = raw.stream().map(entry -> (String) entry.get("schemaname")).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/tables", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tables() {
        String sqlQuery = queries.listTablesQuery();
        List<?> data = database.queryForList(sqlQuery);
        return ResponseEntity.ok(data);
    }
}
