package org.dynapi.dynapi.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/")
public class Home {
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping(value = "/swagger", produces = "text/html")
    public ResponseEntity<?> swagger() {
        InputStream stream = getClass().getResourceAsStream("/static/html/swagger.html");
        if (stream == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to load swagger.html");
        return ResponseEntity.ok(new InputStreamResource(stream));
    }
}
