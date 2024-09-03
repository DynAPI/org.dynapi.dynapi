package org.dynapi.dynapi.core.openapi;

import org.dynapi.openapispec.OpenApiSpecBuilder;

public interface OpenApiProvider {
    void generateOpenAPISpecification(OpenApiSpecBuilder builder);
}
