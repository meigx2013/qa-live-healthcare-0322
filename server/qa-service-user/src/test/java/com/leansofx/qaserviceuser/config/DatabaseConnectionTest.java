package com.leansofx.qaserviceuser.config;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库连接测试类
 */
class DatabaseConnectionTest {

    private static final String URL = "jdbc:mysql://localhost:3301/healthcare?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";

    @Test
    void testDatabaseConnection() {
        Connection connection = null;
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 获取数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            // 验证连接是否成功
            assertNotNull(connection, "数据库连接不应为null");
            assertFalse(connection.isClosed(), "数据库连接应该处于打开状态");
            
            System.out.println("数据库连接测试成功！");
            
        } catch (ClassNotFoundException e) {
            fail("MySQL驱动加载失败: " + e.getMessage());
        } catch (SQLException e) {
            fail("数据库连接失败: " + e.getMessage());
        } finally {
            // 关闭连接
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("关闭数据库连接失败: " + e.getMessage());
                }
            }
        }
    }
}
