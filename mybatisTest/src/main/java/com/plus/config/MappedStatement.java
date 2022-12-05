package com.plus.config;

public class MappedStatement {
    //id
    private Integer id;
    //sql语句
    private String sql;
    //输⼊参数
    private Class<?> paramterType;
    //输出参数
    private Class<?> resultType;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public Class<?> getParamterType() {
        return paramterType;
    }
    public void setParamterType(Class<?> paramterType) {
        this.paramterType = paramterType;
    }
    public Class<?> getResultType() {
        return resultType;
    }
    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }
}
