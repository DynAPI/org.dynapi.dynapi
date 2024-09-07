package org.dynapi.dynapi.core.database.interfaces;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface DetailedTableInformation extends TableInformation {
    /**
     * @return the names of the columns that this table has
     */
    List<String> getColumnNames();

    /**
     * @return information about the columns from this table
     */
    @JsonGetter("column_information")
    List<ColumnInformation> getColumnInformation();
}
