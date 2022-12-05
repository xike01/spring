package com.plus.session.factory;

import com.plus.config.Configuration;
import com.plus.session.DefaultSqlSession;
import com.plus.session.SqlSession;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
