package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TString;

import java.util.UUID;

public class DataTypeUUID implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TString()
                .format(TString.CommonFormats.UUID);
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public Object parseJsonNode(JsonNode node) {
        assert node.isTextual();
        String uuidString = node.asText();
        return UUID.fromString(uuidString);
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public String formatObject(Object object) {
        if (object instanceof UUID u)
            return u.toString();
        throw new IllegalArgumentException("Object is not a UUID");
    }
}
