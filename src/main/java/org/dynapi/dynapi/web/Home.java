package org.dynapi.dynapi.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

/**
 * homepage and other usable html pages
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class Home {
    private DynAPIConfiguration configuration;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> index() {
        return fileAsResponse("/static/html/home.html");
    }

    @GetMapping(value = "/configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> debug() {
        if (!configuration.isDebug())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(new JSONObject(configuration).toString());
    }

    @GetMapping(value = "/redoc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> redoc() {
        return fileAsResponse("/static/html/redoc.html");
    }

    @GetMapping(value = "/swagger", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> swagger() {
        return fileAsResponse("/static/html/swagger.html");

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
