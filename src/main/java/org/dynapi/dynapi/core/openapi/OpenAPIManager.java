package org.dynapi.dynapi.core.openapi;

import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.objects.Contact;
import org.dynapi.openapispec.core.objects.Info;
import org.dynapi.openapispec.core.objects.License;
import org.dynapi.openapispec.core.objects.Server;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenAPIManager {
    protected static List<OpenAPIProvider> providers = new ArrayList<>();

    public static void addProvider(OpenAPIProvider provider) {
        providers.add(provider);
    }

    public static JSONObject generateOpenAPISpecification() {
        OpenApiSpecBuilder specBuilder = new OpenApiSpecBuilder(getInfo());

        String baseUrl = System.getProperty("server.servlet.context-path", "/");
        if (baseUrl.length() > 1)
            specBuilder.addServer(Server.builder()
                    .url(baseUrl)
                    .build()
            );

        for (OpenAPIProvider provider : providers)
            provider.generateOpenAPISpecification(specBuilder);

        return specBuilder.build();
    }

    private static Info getInfo() {
        return Info.builder()
                .title("DynAPI")
                .description("Dynamic API for many Databases")
                .version("0.0.0")
                .contact(Contact.builder()
                        .name("DynAPI")
                        .url("https://github.com/dynapi")
                        .build()
                )
                .license(License.builder()
                        .name("GNU GPLv3")
                        .url("https://choosealicense.com/licenses/gpl-3.0/")
                        .build()
                )
                .build();
    }
}
