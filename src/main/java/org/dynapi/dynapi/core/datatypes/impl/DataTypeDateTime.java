package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DataTypeDateTime implements DataType {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TString()
                .format(TString.CommonFormats.DATETIME);
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public LocalDateTime parseJsonNode(JsonNode node) {
        assert node.isTextual();
        return LocalDateTime.parse(node.textValue(), DATE_FORMATTER);
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public String formatObject(Object object) {
        if (object instanceof LocalDateTime || object instanceof LocalDate || object instanceof Instant)
            return DATE_FORMATTER.format((TemporalAccessor) object);
        throw new IllegalArgumentException("Object is not a date");
    }
}
