package org.dynapi.dynapi.core.openapi;

import org.dynapi.openapispec.OpenApiSpecBuilder;

public interface OpenAPIProvider {
    void generateOpenAPISpecification(OpenApiSpecBuilder builder);
}
