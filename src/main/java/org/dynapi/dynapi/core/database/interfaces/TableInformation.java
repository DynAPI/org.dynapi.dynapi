package org.dynapi.dynapi.core.database.interfaces;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface TableInformation {
    /**
     * @return name of the schema from the table
     */
    @JsonGetter("schema_name")
    String getSchemaName();

    /**
     * @return name of the table
     */
    @JsonGetter("table_name")
    String getTableName();
}
