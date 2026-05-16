package com.velox.framework.persistence.config;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库方言注册表
 */
public class DatabaseDialectRegistry {

    private final Map<String, DatabaseDialect> dialectMap;

    public DatabaseDialectRegistry(List<DatabaseDialect> dialects) {
        this.dialectMap = dialects.stream()
                .collect(Collectors.toUnmodifiableMap(
                        dialect -> normalize(dialect.getType()),
                        Function.identity(),
                        (left, right) -> left
                ));
    }

    public DatabaseDialect getRequiredDialect(String type) {
        DatabaseDialect dialect = dialectMap.get(normalize(type));
        if (dialect == null) {
            throw new IllegalArgumentException("Unsupported database type: " + type);
        }
        return dialect;
    }

    private String normalize(String type) {
        return type == null ? "" : type.trim().toLowerCase(Locale.ROOT);
    }
}
