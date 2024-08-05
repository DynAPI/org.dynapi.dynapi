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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class Home {
    private DynAPIConfiguration configuration;

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping(value = "/debug", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> debug() {
        if (!configuration.isDebug())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(new JSONObject(configuration).toString());
    }

    @GetMapping(value = "/redoc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> redoc() {
        InputStream stream = getClass().getResourceAsStream("/static/html/redoc.html");
        if (stream == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to load redoc.html");
        return ResponseEntity.ok(new InputStreamResource(stream));
    }

    @GetMapping(value = "/swagger", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> swagger() {
        InputStream stream = getClass().getResourceAsStream("/static/html/swagger.html");
        if (stream == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to load swagger.html");
        return ResponseEntity.ok(new InputStreamResource(stream));
    }
}
