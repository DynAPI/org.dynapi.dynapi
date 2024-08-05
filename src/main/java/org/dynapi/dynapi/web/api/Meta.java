package org.dynapi.dynapi.web.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * meta information about the database.
 * like available databases, schemas, tables and columns
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class Meta {

}
