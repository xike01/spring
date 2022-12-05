package com.plus.session.factory;

import com.plus.session.SqlSession;

public interface SqlSessionFactory {
    public SqlSession openSession();
}
