package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TBoolean;

public class DataTypeBoolean implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TBoolean();
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public Object parseJsonNode(JsonNode node) {
        assert node.isBoolean();
        return node.booleanValue();
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public Object formatObject(Object object) {
        if (object instanceof Boolean b)
            return b;
        throw new IllegalArgumentException("Object is not a boolean");
    }
}
