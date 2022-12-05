package com.plus.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    //数据源
    private DataSource dataSource;
    //map集合： key:statementId value:MappedStatement
    private Map<String,MappedStatement> mappedStatementMap = new HashMap<>();
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }
    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
