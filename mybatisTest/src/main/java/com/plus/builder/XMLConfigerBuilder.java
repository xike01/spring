package com.plus.builder;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.plus.config.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigerBuilder {

    private Configuration configuration;
    public XMLConfigerBuilder(Configuration configuration) {
        this.configuration = new Configuration();
    }
    public Configuration parseConfiguration(InputStream inputStream) throws
            DocumentException, PropertyVetoException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream); //<configuation>
        Element rootElement = document.getRootElement();
        List<Node> propertyElements = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Node node : propertyElements) {
            Element propertyElement =  (Element)node;
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.setProperty(name,value);
        }
        //连接池
        ComboPooledDataSource comboPooledDataSource = new
                ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //填充 configuration
        configuration.setDataSource(comboPooledDataSource);
        //mapper 部分
        List<Node> mapperElements = rootElement.selectNodes("//mapper");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        for (Node node : mapperElements) {
            Element mapperElement =  (Element)node;
            String mapperPath = mapperElement.attributeValue("resource");
            InputStream resourceAsSteam =
                    Resources.getResourceAsSteam(mapperPath);
            xmlMapperBuilder.parse(resourceAsSteam);
        }
        return configuration;
    }

}
