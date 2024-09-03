package org.dynapi.dynapi.web.api.delete;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class DeleteController {
    /**
     * deletes one or more entries
     */
    @DeleteMapping(value = "/{schema}/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMultiple(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * deletes one or more entries
     */
    @DeleteMapping(value = "/{schema}/{table}/{rowid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteByRowId(
            HttpServletRequest request,
            @PathVariable("schema") String schemaName,
            @PathVariable("table") String tableName,
            @PathVariable("rowid") long rowid
    ) {
        return ResponseEntity.ok().build();
    }
}
