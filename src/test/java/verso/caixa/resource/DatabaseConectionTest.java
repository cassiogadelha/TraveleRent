package verso.caixa.resource;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class DatabaseConectionTest {
    @Test
    void shouldConnectToPostgresDatabaseBeforeTests() {
        String jdbcUrl = System.getProperty("quarkus.datasource.jdbc.url",
                "jdbc:postgresql://localhost:5432/hibernate_orm_test");
        String username = System.getProperty("quarkus.datasource.username", "quarkus");
        String password = System.getProperty("quarkus.datasource.password", "quarkus");

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            assertTrue(conn.isValid(2), "A conexão com o banco PostgreSQL falhou.");
        } catch (SQLException e) {
            fail("Não foi possível conectar ao banco: " + e.getMessage());
        }
    }

}
