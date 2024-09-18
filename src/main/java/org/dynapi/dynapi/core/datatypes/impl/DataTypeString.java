package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TString;

public class DataTypeString implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TString();
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public String parseJsonNode(JsonNode node) {
        assert node.isTextual();
        return node.textValue();
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public String formatObject(Object object) {
        if (object instanceof String s)
            return s;
        throw new IllegalArgumentException("Object is not a string");
    }
}
