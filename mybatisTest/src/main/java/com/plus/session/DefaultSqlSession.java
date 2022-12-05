package com.plus.session;

import com.plus.config.Configuration;
import com.plus.config.MappedStatement;
import com.plus.exetutor.Executor;
import com.plus.exetutor.SimpleExecutor;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession{
    private Configuration configuration;
    //处理器对象
    private Executor simpleExcutor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public <E> List<E> selectList(String statementId, Object... param) throws Exception {
        MappedStatement mappedStatement =
                configuration.getMappedStatementMap().get(statementId);
        List<E> query = simpleExcutor.query(configuration, mappedStatement, param);
        return query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("返回结果过多");
        }
    }

    @Override
    public void close() throws SQLException {
        simpleExcutor.close();
    }

    @Override
    public <T> T getMappper(Class<?> mapperClass) {
        T o = (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // selectOne
                String methodName = method.getName();
                // className:namespace
                String className = method.getDeclaringClass().getName();
                //statementid
                String key = className+"."+methodName;
                MappedStatement mappedStatement =
                        configuration.getMappedStatementMap().get(key);
                Type genericReturnType = method.getGenericReturnType();
                ArrayList arrayList = new ArrayList();
                //判断是否实现泛型类型参数化
                if(genericReturnType instanceof ParameterizedType){
                    return selectList(key,args);
                }
                return selectOne(key,args);
            }
        });
        return o;
    }
}
