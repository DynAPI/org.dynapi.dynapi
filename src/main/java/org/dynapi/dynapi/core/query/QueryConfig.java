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
     * filter
     */
    private Where[] wheres = null;
    /**
     * maximum amount to query (for pagination)
     */
    private Integer limit = null;
    /**
     * offset for pagination
     */
    private Integer offset = null;
    /**
     * controls the order by
     */
    private OrderBy[] orderBy = null;

    public record Where(String column, Object value) {}
    public record OrderBy(String column, Order order) {}
}
