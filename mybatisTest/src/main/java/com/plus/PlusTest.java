package com.plus;

import com.plus.builder.Resources;
import com.plus.builder.SqlSessionFactoryBuilder;
import com.plus.session.SqlSession;
import com.plus.session.factory.SqlSessionFactory;
import com.pub.dos.UserDo;

import java.io.InputStream;
import java.util.List;

public class PlusTest {

    public static void main(String[] args) throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = build.openSession();
        UserDo user = new UserDo();
        user.setId(1);
        user.setUsername("tom");
        List<Object> objects = sqlSession.selectList("UserMapper.selectList", "");
        for (Object o:objects){
            UserDo userDo = (UserDo) o;
            System.out.println(userDo);
        }
        //代理对象
        /*UserMapper userMapper = sqlSession.getMappper(UserMapper.class);
        User userl = userMapper.selectOne(user);
        System・out.println(userl);*/
    }

}
