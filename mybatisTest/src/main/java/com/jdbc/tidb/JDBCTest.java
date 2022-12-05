package com.jdbc.tidb;

import com.pub.dos.UserDo;

import java.sql.*;

/**
 * JDBC连接数据库
 */
public class JDBCTest {

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserDo user = new UserDo();
        try {
            //TODO 数据库连接 硬编码 + 频繁创建释放资源浪费， 使用数据库连接池初始化连接资源
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 通过驱动管理类获取数据库链接
            connection = DriverManager.getConnection("jdbc:mysql://192.168.3.27:4000/test?characterEncoding=utf-8",
                    "root", "123");
            //TODO sql语句、参数、结果集 硬编码， sql抽取到xml配置文件， 参数和结果集使用反射/内省
            //定义sql语句？表示占位符
            String sql = "select * from user where username = ?";
            // 获取预处理statement
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数，第⼀个参数为sql语句中参数的序号(从1开始)，第⼆个参数为设置的参数值
            preparedStatement.setString(1, "tom");
            // 向数据库发出sql执⾏查询，查询出结果集
            resultSet = preparedStatement.executeQuery();
            // 遍历查询结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                // 封装User
                user.setId(id);
                user.setUsername(username);
            }
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
