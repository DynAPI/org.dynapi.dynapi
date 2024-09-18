package org.dynapi.dynapi.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.dynapi.dynapi.core.openapi.OpenApiManager;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class Home {
    private DynAPIConfiguration configuration;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> index() {
        return fileAsResponse("/html/home.html");
    }

    @GetMapping(value = "/redoc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> redoc() {
        if (!configuration.getWeb().isRedoc())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return fileAsResponse("/html/redoc.html");
    }

    @GetMapping(value = "/swagger", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> swagger() {
        if (!configuration.getWeb().isSwagger())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return fileAsResponse("/html/swagger.html");
    }

    @GetMapping(value = "/openapi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOpenApi() {
        if (!configuration.getWeb().isOpenapi())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String spec = OpenApiManager.generateOpenAPISpecification().toString();
        return ResponseEntity.ok(spec);
    }

    private ResponseEntity<?> fileAsResponse(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            String name = path.substring(path.lastIndexOf('/') + 1);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to load " + name);
        }
        return ResponseEntity.ok(new InputStreamResource(stream));
    }
}
