package dev.alex96jvm.javaintensive.dao.impl;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class DatabaseConnectionTest {
    @Test
    public void testGetConnection() {
        Connection mockedConnection = Mockito.mock(Connection.class);
        try (MockedStatic<DriverManager> mockedStatic = mockStatic(DriverManager.class)) {
            mockedStatic.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockedConnection);

            Connection connection = DatabaseConnection.getConnection();

            assertNotNull(connection);
            mockedStatic.verify(() -> DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/javaintensive",
                    "postgres",
                    "123"));
        }
    }
}


