package org.dynapi.dynapi.core.datatypes.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.dynapi.dynapi.core.datatypes.DataType;
import org.dynapi.openapispec.core.schema.Schema;
import org.dynapi.openapispec.core.schema.TString;

import java.util.Base64;

public class DataTypeBlob implements DataType {
    /**
     * @return schema for /openapi
     */
    @Override
    public Schema<?, ?> getOpenApiSchema() {
        return new TString()
                .format(TString.CommonFormats.BINARY);
    }

    /**
     * @param node node to parse
     * @return parsed value
     */
    @Override
    public byte[] parseJsonNode(JsonNode node) {
        assert node.isTextual();
        String base64String = node.textValue();
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * @param object saved or so object
     * @return json-save object
     */
    @Override
    public Object formatObject(Object object) {
        if (object instanceof byte[] b)
            return Base64.getEncoder().encodeToString(b);
        throw new IllegalArgumentException("Object is not a blob");
    }
}
