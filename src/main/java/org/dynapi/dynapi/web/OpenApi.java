package org.dynapi.dynapi.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.openapispec.core.objects.Info;
import org.dynapi.openapispec.core.objects.Tag;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.*;
import org.dynapi.openapispec.core.types.*;

/**
 * /openapi specification endpoint
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class OpenApi {
    @GetMapping(value = "/openapi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOpenApi() {
        Info info = Info.builder()
                .title("DynAPI")
                .version("0.0")
                .build();
        OpenApiSpecBuilder spec = new OpenApiSpecBuilder(info);
        try {
            String baseUrl = System.getProperty("server.servlet.context-path", "/");
            if (baseUrl.length() > 1)
                spec.addServer(new ServerBuilder()
                        .url(baseUrl)
                        .build()
                );
            spec.addTag(Tag.builder()
                    .name("common")
                    .description("Common Operations")
                    .build()
            );
            spec.registerRefResponse("500", new ResponseBuilder()
                    .addContent(MediaType.APPLICATION_JSON_VALUE, new TObject()
                                    .example(new JSONObject()
                                            .put("error", RuntimeException.class.getName())
                                            .put("detail", "example")
                                    )
                                    .addProperty("error", new TString()
                                            .description("Error Class Identifier")
                                            .example("RuntimeError")
                                    )
                                    .addProperty("detail", new TString()
                                            .description("Detailed information about what caused the error")
                                    )
                    )
                    .build()
            );
            spec.addPath(
                    "/example",
                    new PathBuilder()
                            .addMethod(OperationType.GET, new OperationBuilder()
                                    .addTag("common")
                                    .addResponse(200, new ResponseBuilder()
                                            .addContent(MediaType.APPLICATION_JSON_VALUE, new TObject()
                                                    .example(new JSONObject()
                                                            .put("text", "Hello World"))
                                                    .addProperty("text", new TString()
                                                            .options("Hello World")
                                                    )
                                            )
                                            .build()
                                    )
                                    .addResponse(500, new ResponseBuilder()
                                            .addContent(MediaType.APPLICATION_JSON_VALUE, new TRef(ComponentType.RESPONSE, "500"))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
            );
        } catch (Exception error) {
            // internal server error would be the better status but then the error-formatted openapi-specification
            // would not be displayed
            return ResponseEntity.status(HttpStatus.OK).body(spec.build(error).toString());
        }
        return ResponseEntity.ok(spec.build().toString());
    }
}
