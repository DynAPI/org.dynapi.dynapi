package org.dynapi.dynapi.core.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.dynapi.squirtle.core.enums.Order;

import java.util.Enumeration;

public class QueryConfigParser {
    public static QueryConfig parse(HttpServletRequest request) {
        QueryConfig queryConfig = new QueryConfig();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            switch (parameterName) {
                case "column" -> queryConfig.setColumns(request.getParameterValues(parameterName));
                case "columns" -> queryConfig.setColumns(parseColumns(request.getParameter(parameterName)));
                case "limit" -> queryConfig.setLimit(Integer.parseInt(request.getParameter(parameterName)));
                case "offset" -> queryConfig.setOffset(Integer.parseInt(request.getParameter(parameterName)));
                case "order_by" -> queryConfig.setOrderBy(parseOrderBy(request.getParameterValues(parameterName)));
            }
        }

        return queryConfig;
    }

    /**
     * parse the 'columns' header field which is a string-array
     */
    private static String[] parseColumns(String jsonString) {
        final JsonNode jsonNode;
        try {
            jsonNode = (new ObjectMapper()).readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not parse JSON string.", e);
        }
        if (!jsonNode.isArray()) throw new IllegalArgumentException("JSON is not an array.");
        String[] columns = new String[jsonNode.size()];

        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode elementNode = jsonNode.get(i);
            if (!elementNode.isTextual()) throw new IllegalArgumentException("JSON array element is not a string.");
            columns[i] = elementNode.textValue();
        }

        return columns;
    }

    /**
     * parses the 'order_by' header values <br>
     * format: 'header-name' | 'header-name;[asc|ASC|desc|DESC]'
     */
    private static QueryConfig.OrderBy[] parseOrderBy(String[] values) {
        QueryConfig.OrderBy[] orderBys = new QueryConfig.OrderBy[values.length];
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            int sepIndex = value.indexOf(";");
            if (sepIndex == -1) {
                orderBys[i] = new QueryConfig.OrderBy(value, null);
            } else if (value.indexOf(";", sepIndex + 1) != -1) {
                throw new IllegalArgumentException("Invalid 'order_by' format: " + value);
            } else {
                final String columnName = value.substring(0, sepIndex);
                final Order order = switch (value.substring(sepIndex + 1)) {
                    case "asc", "ASC" -> Order.ASC;
                    case "desc", "DESC" -> Order.DESC;
                    default -> throw new IllegalArgumentException("Invalid 'order_by' order: " + value);
                };
                orderBys[i] = new QueryConfig.OrderBy(columnName, order);
            }
        }
        return orderBys;
    }
}
