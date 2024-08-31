package org.dynapi.dynapi.web.api.patch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.openapi.OpenApiManager;
import org.dynapi.dynapi.core.openapi.OpenApiProvider;
import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PatchOpenApiProvider implements OpenApiProvider {
    @PostConstruct
    public void init() {
        OpenApiManager.addProvider(this);
    }

    @Override
    public void generateOpenAPISpecification(OpenApiSpecBuilder builder) {

    }
}
