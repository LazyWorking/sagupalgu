package com.lazyworking.sagupalgu.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@SpringBootTest
public class ConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection(){
        try {
            System.out.println("dataSource = " + dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
