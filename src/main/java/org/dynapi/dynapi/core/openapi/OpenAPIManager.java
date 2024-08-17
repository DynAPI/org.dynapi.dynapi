package org.dynapi.dynapi.core.openapi;

import org.dynapi.openapispec.OpenApiSpecBuilder;
import org.dynapi.openapispec.core.objects.Contact;
import org.dynapi.openapispec.core.objects.Info;
import org.dynapi.openapispec.core.objects.License;
import org.dynapi.openapispec.core.objects.Server;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAPIManager {
    protected static List<OpenAPIProvider> providers = new ArrayList<>();

    public static void addProvider(OpenAPIProvider provider) {
        providers.add(provider);
    }

    public static JSONObject generateOpenAPISpecification() {
        Info info = getInfo();
        OpenApiSpecBuilder specBuilder = new OpenApiSpecBuilder(info);

        String baseUrl = System.getProperty("server.servlet.context-path", "/");
        if (baseUrl.length() > 1)
            specBuilder.addServer(Server.builder()
                    .url(baseUrl)
                    .build()
            );

        Map<String, Throwable> generationErrors = new HashMap<>();

        for (OpenAPIProvider provider : providers) {
            try {
                provider.generateOpenAPISpecification(specBuilder);
            } catch (Throwable e) {
                generationErrors.put(provider.getClass().getCanonicalName(), e);
            }
        }

        if (!generationErrors.isEmpty()) {
            String errorDescription = String.format(
                    "%s\n\n## Generation Errors\n\n%s",
                    info.description,
                    String.join("\n\n",
                            generationErrors.entrySet().stream().map(entry -> {
                                Throwable error = entry.getValue();
                                return String.format("### %s\n**%s**: %s", entry.getKey(), error.getClass().getCanonicalName(), error.getMessage());
                            }).toList())
            );
            return new OpenApiSpecBuilder(info.withDescription(errorDescription)).build();
        }

        return specBuilder.build();
    }

    private static Info getInfo() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeNow = LocalDateTime.now().format(dateTimeFormatter);

        return Info.builder()
                .title("DynAPI")
                .summary("Dynamic API for many Databases")
                .description(String.format("Generated: %s", timeNow))
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
