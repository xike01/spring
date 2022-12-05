package com.plus.builder;

import com.plus.config.Configuration;
import com.plus.session.factory.DefaultSqlSessionFactory;
import com.plus.session.factory.SqlSessionFactory;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }
    public SqlSessionFactory build(InputStream inputStream) throws
            DocumentException, PropertyVetoException, ClassNotFoundException {
        //1.解析配置⽂件，封装Configuration
        XMLConfigerBuilder xmlConfigerBuilder = new XMLConfigerBuilder(configuration);

        Configuration configuration =
                xmlConfigerBuilder.parseConfiguration(inputStream);
        //2.创建 sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new
                DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }
}
