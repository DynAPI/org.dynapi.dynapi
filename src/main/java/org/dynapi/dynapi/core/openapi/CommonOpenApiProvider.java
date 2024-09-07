package org.dynapi.dynapi.core.openapi;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.ResponseBuilder;
import org.dynapi.openapispec.core.objects.Server;
import org.dynapi.openapispec.core.schema.TInteger;
import org.dynapi.openapispec.core.schema.TObject;
import org.dynapi.openapispec.core.schema.TString;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommonOpenApiProvider implements OpenApiProvider {
    private final DynAPIConfiguration configuration;

    @PostConstruct
    public void init() {
        OpenApiManager.providers.add(this);
    }

    @Override
    public void generateOpenAPISpecification(OpenApiSpecBuilder builder) {
        builder.addServer(Server.builder()
                .url(configuration.getServer().getBaseurl())
                .build()
        );

        TObject basicErrorResponseSchema = new TObject()
                .addProperty("timestamp",
                        new TString()
                                .format(TString.CommonFormats.DATETIME)
                                .description("timestamp of the request")
                                .example("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                )
//                .addProperty("status", new TInteger())
//                .addProperty("error", new TString())
                .addProperty("message",
                        new TString()
                                .description("details about the error")
                                .example("This server refuses to brew coffee because it is, permanently, a teapot")
                )
                .addProperty("path",
                        new TString()
                                .description("endpoint of the current request")
                );

        for (HttpStatus httpStatus : HttpStatus.values()) {
            if (!httpStatus.is4xxClientError() && !httpStatus.is5xxServerError()) continue;
            TObject contentSchema = basicErrorResponseSchema.copy()
                    .addProperty("status",
                            new TInteger()
                                    .description("HTTP status code")
                                    .options(httpStatus.value())
                    )
                    .addProperty("error",
                            new TInteger()
                                    .description("HTTP status code")
                                    .options(httpStatus.getReasonPhrase())
                    );
            builder.registerRefResponse(String.valueOf(httpStatus.value()), new ResponseBuilder()
                    .description(httpStatus.getReasonPhrase())
                    .addContent(contentSchema)
                    .build()
            );
        }
    }
}
