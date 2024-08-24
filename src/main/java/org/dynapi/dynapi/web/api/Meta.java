package org.dynapi.dynapi.web.api;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.db.meta.MetaQueryGenerator;
import org.dynapi.dynapi.core.openapi.OpenAPIManager;
import org.dynapi.dynapi.core.openapi.OpenAPIProvider;
import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.*;
import org.dynapi.openapispec.core.objects.*;
import org.dynapi.openapispec.core.types.*;
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
public class Meta implements OpenAPIProvider {
    private JdbcTemplate database;
    private MetaQueryGenerator queries;

    @PostConstruct
    public void init() {
        OpenAPIManager.addProvider(this);
    }

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

    @Override
    public void generateOpenAPISpecification(OpenApiSpecBuilder builder) {
        builder.addTag(Tag.builder()
                .name("meta")
                .description("Endpoints related to query information about the database")
                .build()
        );

        builder.addPath("/schemas", new PathBuilder()
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists available schemas")
                        .description("Lists information about the available schemas")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TString())
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/tables", new PathBuilder()
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists tables and their schema")
                        .description("Lists information about the available tables")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("schemaname", new TString())
                                                .addProperty("tablename", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/tables/{schema}", new PathBuilder()
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("schema")
                        .description("Name of the database schema")
                        .schema(new TString())
                        .build()
                )
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists tables if a schema")
                        .description("Lists information about the tables of a schema")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("schemaname", new TString())
                                                .addProperty("tablename", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/columns/{schema}/{table}", new PathBuilder()
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("schema")
                        .description("Name of the database schema")
                        .schema(new TString())
                        .build()
                )
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("table")
                        .description("Name of the database table")
                        .schema(new TString())
                        .build()
                )
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists columns of a table")
                        .description("Lists information about the columns of a table")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("column_name", new TString())
                                                .addProperty("type", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );
    }
}
