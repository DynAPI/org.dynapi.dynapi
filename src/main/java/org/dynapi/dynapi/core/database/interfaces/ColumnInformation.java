package org.dynapi.dynapi.core.database.interfaces;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ColumnInformation {
    /**
     * @return name of the column
     */
    @JsonGetter("column_name")
    String getColumnName();

    /**
     * @return column type directly used in the database
     */
    @JsonGetter("type")
    String getRawColumnType();
}
