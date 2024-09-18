package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TArray;

public class DataTypeArray implements DataType {
    private final DataType subType;

    public DataTypeArray(DataType subType) {
        this.subType = subType;
    }

    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TArray(subType.getOpenApiSchema());
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public Object[] parseJsonNode(JsonNode node) {
        assert node.isArray();
        Object[] array = new Object[node.size()];
        for (int i = 0; i < node.size(); i++) {
            JsonNode element = node.get(i);
            array[i] = subType.parseJsonNode(element);
        }
        return array;
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public Object formatObject(Object object) {
        if (object instanceof Object[] a) {
            Object[] array = new Object[a.length];
            for (int i = 0; i < a.length; i++)
                array[i] = subType.formatObject(a[i]);
            return array;
        }
        throw new IllegalArgumentException("Object is not an array");
    }
}
