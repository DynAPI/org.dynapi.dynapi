package org.dynapi.dynapi.core.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dynapi.dynapi.core.config.DynAPIConfiguration;
import org.dynapi.squirtle.core.enums.Order;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Slf4j
@Component
@AllArgsConstructor
public class QueryConfigParser {
    private final DynAPIConfiguration configuration;

    public QueryConfig parse(HttpServletRequest request) {
        final QueryConfig queryConfig = new QueryConfig();

        final Enumeration<String> parameterNames = request.getParameterNames();

        final DynAPIConfiguration.ApiConfiguration apiConfiguration = configuration.getApi();
        final Integer pageSize = apiConfiguration.getPageSize();
        if (apiConfiguration.isApplyDefaultPagination())
            queryConfig.setLimit(pageSize);
        final Integer maxLimit = apiConfiguration.getMaxLimit();
        if (maxLimit != null)
            queryConfig.setLimit(maxLimit);

        while (parameterNames.hasMoreElements()) {
            final String parameterName = parameterNames.nextElement();
            switch (parameterName) {
                case "column" -> queryConfig.setColumns(request.getParameterValues(parameterName));
                case "columns" -> queryConfig.setColumns(parseColumns(request.getParameter(parameterName)));
                case "where" -> queryConfig.setWheres(parseWheres(request.getParameterValues(parameterName)));
                case "limit" -> {
                    int limit = Integer.parseInt(request.getParameter(parameterName));
                    if (maxLimit != null && limit > maxLimit)
                        throw new IllegalArgumentException("Limit must be less than " + maxLimit + " but was " + limit);
                    queryConfig.setLimit(limit);
                }
                case "offset" -> queryConfig.setOffset(Integer.parseInt(request.getParameter(parameterName)));
                case "order_by" -> queryConfig.setOrderBy(parseOrderBy(request.getParameterValues(parameterName)));
                case "page" -> {
                    if (pageSize == null) continue;
                    final int page = Integer.parseInt(request.getParameter(parameterName));
                    queryConfig.setLimit(pageSize);
                    queryConfig.setOffset(page * pageSize);
                }
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

    private static QueryConfig.Where[] parseWheres(String[] jsonStrings) {
        QueryConfig.Where[] wheres = new QueryConfig.Where[jsonStrings.length];
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < jsonStrings.length; i++) {
            String jsonString = jsonStrings[i];
            JsonNode node;
            try {
                node = objectMapper.readTree(jsonString);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("'where' is not json", e);
            }
            if (!node.isArray()) throw new IllegalArgumentException("'where' is not an array.");
            if (node.size() != 2) throw new IllegalArgumentException("'where' does not match [column,value].");

            JsonNode columnNode = node.get(0);
            if (!columnNode.isTextual()) throw new IllegalArgumentException("'where'@column is not a string.");
            String column = columnNode.textValue();
            if (column.isBlank()) throw new IllegalArgumentException("'where'@column is bad");

            JsonNode valueNode = node.get(1);
            Object value = switch (valueNode.getNodeType()) {
                case STRING -> valueNode.textValue();
                case NUMBER -> valueNode.numberValue();
                case BOOLEAN -> valueNode.booleanValue();
                case NULL -> null;
                default -> throw new IllegalArgumentException("'where'@value has an unsupported type.");
            };
            wheres[i] = new QueryConfig.Where(column, value);
        }
        return wheres;
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
