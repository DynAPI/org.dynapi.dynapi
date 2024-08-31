package org.dynapi.dynapi.web.meta;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.openapi.OpenApiManager;
import org.dynapi.dynapi.core.openapi.OpenApiProvider;
import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.OperationBuilder;
import org.dynapi.openapispec.core.OperationType;
import org.dynapi.openapispec.core.PathBuilder;
import org.dynapi.openapispec.core.ResponseBuilder;
import org.dynapi.openapispec.core.objects.Parameter;
import org.dynapi.openapispec.core.objects.Tag;
import org.dynapi.openapispec.core.types.TArray;
import org.dynapi.openapispec.core.types.TObject;
import org.dynapi.openapispec.core.types.TString;

@Slf4j
public class MetaOpenApiProvider implements OpenApiProvider {

    @PostConstruct
    public void init() {
        OpenApiManager.addProvider(this);
    }

    @Override
    public void generateOpenAPISpecification(OpenApiSpecBuilder builder) {
        builder.addTag(Tag.builder()
                .name("meta")
                .description("Endpoints related to query information about the database")
                .build()
        );

        builder.addPath("/schemas", new PathBuilder()
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists available schemas")
                        .description("Lists information about the available schemas")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TString())
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/tables", new PathBuilder()
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists tables and their schema")
                        .description("Lists information about the available tables")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("schemaname", new TString())
                                                .addProperty("tablename", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/tables/{schema}", new PathBuilder()
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("schema")
                        .description("Name of the database schema")
                        .schema(new TString())
                        .build()
                )
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists tables if a schema")
                        .description("Lists information about the tables of a schema")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("schemaname", new TString())
                                                .addProperty("tablename", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );

        builder.addPath("/columns/{schema}/{table}", new PathBuilder()
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("schema")
                        .description("Name of the database schema")
                        .schema(new TString())
                        .build()
                )
                .addCommonParameter(Parameter.builder()
                        .in(Parameter.In.PATH)
                        .required(true)
                        .name("table")
                        .description("Name of the database table")
                        .schema(new TString())
                        .build()
                )
                .addMethod(OperationType.GET, new OperationBuilder()
                        .addTag("meta")
                        .summary("Lists columns of a table")
                        .description("Lists information about the columns of a table")
                        .addResponse(200, new ResponseBuilder()
                                .description("Success")
                                .addContent(new TArray()
                                        .addType(new TObject()
                                                .addProperty("column_name", new TString())
                                                .addProperty("type", new TString())
                                        )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        );
    }
}
