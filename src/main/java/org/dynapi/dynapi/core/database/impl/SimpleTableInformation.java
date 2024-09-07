package org.dynapi.dynapi.core.database.impl;

import lombok.*;
import org.dynapi.dynapi.core.database.interfaces.TableInformation;

/**
 * simple implementation of {@link TableInformation}
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleTableInformation implements TableInformation {
    private final String schemaName;
    private final String tableName;
}
