package org.dynapi.dynapi.web.meta;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.interfaces.ColumnInformation;
import org.dynapi.dynapi.core.database.interfaces.Database;
import org.dynapi.dynapi.core.database.interfaces.TableInformation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/meta")
public class MetaController {
    private Database database;

    @GetMapping(value = "/schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> schemas() {
        List<String> schemaNames = database.getSchemaNames();
        return ResponseEntity.ok(schemaNames);
    }

    @GetMapping(value = "/tables", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tables() {
        List<TableInformation> tableInformations = database.getTableInformation();
        return ResponseEntity.ok(tableInformations);
    }

    @GetMapping(value = "/tables/{schema-name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> tablesOfSchema(
            @PathVariable("schema-name") String schemaName
    ) {
        List<TableInformation> tableInformations = database.getTableInformationOfSchema(schemaName);
        return ResponseEntity.ok(tableInformations);
    }

    @GetMapping(value = "/columns/{schema-name}/{table-name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> columns(
            @PathVariable("schema-name") String schemaName,
            @PathVariable("table-name") String tableName
    ) {
        List<ColumnInformation> columnInformations = database.getColumnInformationOfTable(schemaName, tableName);
        return ResponseEntity.ok(columnInformations);
    }
}
