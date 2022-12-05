package com.plus.exetutor;

import com.plus.config.Configuration;
import com.plus.config.MappedStatement;
import com.plus.utils.GenericTokenParser;
import com.plus.utils.ParameterMapping;
import com.plus.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{
    private Connection connection = null;
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception {
        //获取连接
        connection = configuration.getDataSource().getConnection();
        // select * from user where id = #{id} and username = #{username}
        String sql = mappedStatement.getSql();
        //对sql进⾏处理
        BoundSql boundsql = getBoundSql(sql);
        // select * from where id = ? and username = ?
        String finalSql = boundsql.getSqlText();
        //获取传⼊参数类型
        Class<?> paramterType = mappedStatement.getParamterType();
        //获取预编译preparedStatement对象
        PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
        List<ParameterMapping> parameterMappingList =
                boundsql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String name = parameterMapping.getContent();
            //反射
            Field declaredField = paramterType.getDeclaredField(name);
            declaredField.setAccessible(true);
            //参数的值
            Object o = declaredField.get(param[0]);
            //给占位符赋值
            preparedStatement.setObject(i + 1, o);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        Class<?> resultType = mappedStatement.getResultType();
        ArrayList<E> results = new ArrayList<E>();
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            E o = (E) resultType.newInstance();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                //属性名
                String columnName = metaData.getColumnName(i);
                //属性值
                Object value = resultSet.getObject(columnName);
                //创建属性描述器，为属性⽣成读写⽅法
                PropertyDescriptor propertyDescriptor = new
                        PropertyDescriptor(columnName, resultType);
                //获取写⽅法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                //向类中写⼊值
                writeMethod.invoke(o, value);
            }
            results.add(o);
        }
        return results;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    private BoundSql getBoundSql(String sql) {
        //标记处理类：主要是配合通⽤标记解析器GenericTokenParser类完成对配置⽂件等的解 析⼯作，其中TokenHandler主要完成处理
        ParameterMappingTokenHandler parameterMappingTokenHandler = new
                ParameterMappingTokenHandler();
        //GenericTokenParser :通⽤的标记解析器，完成了代码⽚段中的占位符的解析，然后再根 据给定的标记处理器(TokenHandler)来进⾏表达式的处理
        //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记处 理器)
        GenericTokenParser genericTokenParser = new GenericTokenParser("# {", "}",
                parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings =
                parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse, parameterMappings);
        return boundSql;
    }
}
