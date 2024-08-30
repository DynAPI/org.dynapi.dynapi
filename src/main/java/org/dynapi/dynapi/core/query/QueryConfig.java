package org.dynapi.dynapi.core.query;

import lombok.Data;
import org.dynapi.squirtle.core.enums.Order;

@Data
public class QueryConfig {
    /**
     * column names to query
     */
    private String[] columns = new String[]{"*"};
    /**
     * maximum amount to query (for pagination)
     */
    private Long limit = null;
    /**
     * offset for pagination
     */
    private Long offset = null;
    /**
     * controls the order by
     */
    private OrderBy[] orderBy = null;

    public record OrderBy(String column, Order order) {};
}
