package org.dynapi.dynapi.core.datatypes;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.openapispec.core.schema.Schema;

public interface DataType {
    /**
     * @return schema for /openapi
     */
    Schema<?, ?> getOpenApiSchema();

    /**
     * @param node node to parse
     * @return parsed value
     */
    Object parseJsonNode(JsonNode node);

    /**
     * @param object saved or so object
     * @return json-save object
     */
    Object formatObject(Object object);
}
