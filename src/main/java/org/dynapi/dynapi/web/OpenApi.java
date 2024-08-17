package org.dynapi.dynapi.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.openapi.OpenAPIManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String spec = OpenAPIManager.generateOpenAPISpecification().toString();
        return ResponseEntity.ok(spec);
    }
}
