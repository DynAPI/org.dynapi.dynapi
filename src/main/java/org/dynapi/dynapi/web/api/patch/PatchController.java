package org.dynapi.dynapi.web.api.patch;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class PatchController {
    /**
     * partially modifies a resource
     */
    @PatchMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchMultiple(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * partially modifies a resource
     */
    @PatchMapping(value = "/{schema}/{table}/{rowid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchByRowId(
            HttpServletRequest request,
            @PathVariable String schema,
            @PathVariable String table,
            @PathVariable("rowid") long rowid
    ) {
        return ResponseEntity.ok().build();
    }
}
