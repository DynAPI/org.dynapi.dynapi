package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TInteger;

public class DataTypeInteger implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TInteger();
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public Number parseJsonNode(JsonNode node) {
        assert node.isIntegralNumber();
        return node.isLong() ? node.longValue() : node.intValue();
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public Object formatObject(Object object) {
        if (object instanceof Long l)
            return l;
        if (object instanceof Integer i)
            return i;
        throw new IllegalArgumentException("Object is not an integer");
    }
}
