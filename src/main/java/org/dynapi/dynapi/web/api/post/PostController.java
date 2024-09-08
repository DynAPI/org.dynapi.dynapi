package org.dynapi.dynapi.web.api.post;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.database.interfaces.ColumnInformation;
import org.dynapi.dynapi.core.database.interfaces.Database;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.parameters.QmarkParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final Database database;
    private final JdbcTemplate jdbcTemplate;

    /**
     * adds one or more entries
     */
    @Transactional
    @PostMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMultiple(
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName,
            @RequestBody JsonNode postBody
    ) {
        List<ColumnInformation> tableColumns = database.getColumnInformationOfTable(schemaName, tableName);

        Schema schema = new Schema(schemaName);
        Table table = schema.table(tableName);
        String sql = database.newQuery()
                .into(table)
                .columns(tableColumns.stream().map(col -> (SqlAble) table.field(col.getColumnName())).toList())
                .insert(tableColumns.stream().map(ignored -> new QmarkParameter()).toList())
                .getSql();
        log.info(sql);

        if (postBody.isObject()) {  // insert single entry
            Object[] values = getValues(tableColumns, postBody);
            jdbcTemplate.update(sql, values);
        } else if (postBody.isArray()) {  // insert batch of entries
            batchInsertCheckIfAllAreObject(postBody);

            // todo: improve with BatchPreparedStatementSetter instead of List<Object[]>
            List<Object[]> batchValues = new ArrayList<>();
            for (int i = 0; i < postBody.size(); i++) {
                JsonNode element = postBody.get(i);
                Object[] values = getValues(tableColumns, element);
                batchValues.add(values);
            }
            jdbcTemplate.batchUpdate(sql, batchValues);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad body. Has to be object or array");
        }

        return ResponseEntity.noContent().build();
    }

    protected void batchInsertCheckIfAllAreObject(JsonNode arrayNode) throws ResponseStatusException {
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode element = arrayNode.get(i);
            if (!element.isObject())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch element " + i + " is not an object");
        }
    }

    protected Object[] getValues(List<ColumnInformation> columns, JsonNode node) {
        Object[] vals = new Object[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            ColumnInformation column = columns.get(i);
            String columnName = column.getColumnName();
            vals[i] = getValue(column, node.get(columnName));
        }
        return vals;
    }

    protected Object getValue(ColumnInformation columnInformation, JsonNode node) {
        if (node.isMissingNode() || node.isNull())
            return null;
        // todo: requires ColumnInformation#getSimplifiedType
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad body. can't convert field");
    }
}
