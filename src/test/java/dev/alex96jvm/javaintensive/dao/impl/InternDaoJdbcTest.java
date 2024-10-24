package dev.alex96jvm.javaintensive.dao.impl;

import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class InternDaoJdbcTest {
    private Connection connection;
    private InternDaoJdbc internDaoJdbc;

    @BeforeEach
    void setUp() throws Exception {
        JdbcConnectionPool cp = JdbcConnectionPool.create(
                "jdbc:h2:mem:testdb", "sa", "");
        connection = cp.getConnection();
        internDaoJdbc = new InternDaoJdbc(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE interns (id BIGINT AUTO_INCREMENT PRIMARY KEY, firstname VARCHAR(40) NOT NULL, lastname VARCHAR(40) NOT NULL)");
            stmt.execute("CREATE TABLE marks (id BIGINT AUTO_INCREMENT PRIMARY KEY, subject VARCHAR(40) NOT NULL, mark INT NOT NULL CHECK (mark >= 1 AND mark <= 5), intern_id BIGINT NOT NULL, FOREIGN KEY (intern_id) REFERENCES interns(id) ON DELETE CASCADE)");
            stmt.execute("INSERT INTO interns (firstname, lastname) VALUES ('Ivan', 'Ivanov')");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE marks");
            stmt.execute("DROP TABLE interns");
        }
        connection.close();
    }

    @Test
    void testReadAll() {
        List<InternEntity> interns = internDaoJdbc.readAll();

        assertEquals(1, interns.size());
        assertEquals("Ivan", interns.get(0).getFirstName());
        assertEquals("Ivanov", interns.get(0).getLastName());
    }

    @Test
    void testCreateIntern() {
        InternEntity newIntern = new InternEntity(null, "Ivan", "Ivanov", List.of());
        InternEntity createdIntern = internDaoJdbc.create(newIntern);

        assertNotNull(createdIntern.getId());
        assertEquals("Ivan", createdIntern.getFirstName());
        assertEquals("Ivanov", createdIntern.getLastName());
    }

    @Test
    void testReadById() {
        InternEntity newIntern = new InternEntity(null, "Petr", "Petrov", List.of());
        InternEntity createdIntern = internDaoJdbc.create(newIntern);

        Optional<InternEntity> retrievedIntern = internDaoJdbc.readById(createdIntern.getId());

        assertTrue(retrievedIntern.isPresent());
        assertEquals("Petr", retrievedIntern.get().getFirstName());
        assertEquals("Petrov", retrievedIntern.get().getLastName());
    }

    @Test
    void testUpdateMarks() {
        InternEntity newIntern = new InternEntity(null, "Sergey", "Sergeev", List.of());
        InternEntity createdIntern = internDaoJdbc.create(newIntern);

        MarkEntity newMark = new MarkEntity(null, "SQL", 5, createdIntern.getId());
        Optional<InternEntity> updatedIntern = internDaoJdbc.update(newMark);

        assertTrue(updatedIntern.isPresent());
        assertEquals(1, updatedIntern.get().getMarks().size());
        assertEquals("SQL", updatedIntern.get().getMarks().get(0).getSubject());
        assertEquals(5, updatedIntern.get().getMarks().get(0).getMark());
    }

    @Test
    void testDeleteIntern() {
        InternEntity newIntern = new InternEntity(null, "Andrey", "Andreev", List.of());
        InternEntity createdIntern = internDaoJdbc.create(newIntern);

        Boolean isDeleted = internDaoJdbc.delete(createdIntern.getId());
        assertTrue(isDeleted);

        Optional<InternEntity> deletedIntern = internDaoJdbc.readById(createdIntern.getId());
        assertFalse(deletedIntern.isPresent());
    }
}
