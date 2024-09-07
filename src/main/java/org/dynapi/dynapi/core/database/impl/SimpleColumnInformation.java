package org.dynapi.dynapi.core.database.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.dynapi.dynapi.core.database.interfaces.ColumnInformation;

/**
 * simple implementation of {@link ColumnInformation}
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleColumnInformation implements ColumnInformation {
    private final String columnName;
    private final String rawColumnType;
}
