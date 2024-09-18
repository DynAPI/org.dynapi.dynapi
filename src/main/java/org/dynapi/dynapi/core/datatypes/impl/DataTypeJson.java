package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TObject;

public class DataTypeJson implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TObject();
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public JsonNode parseJsonNode(JsonNode node) {
        return node;
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public JsonNode formatObject(Object object) {
        if (object instanceof JsonNode j)
            return j;
        throw new IllegalArgumentException("Object is not a JSON object");
    }
}
