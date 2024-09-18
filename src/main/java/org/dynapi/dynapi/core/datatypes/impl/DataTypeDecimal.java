package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TNumber;

public class DataTypeDecimal implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TNumber();
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public Number parseJsonNode(JsonNode node) {
        assert node.isFloatingPointNumber();
        return node.isDouble() ? node.doubleValue() : node.floatValue();
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public Object formatObject(Object object) {
        if (object instanceof Double d)
            return d;
        if (object instanceof Float f)
            return f;
        throw new IllegalArgumentException("Object is not a decimal");
    }
}
