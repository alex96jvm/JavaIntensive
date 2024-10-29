package dev.alex96jvm.javaintensive.dao.impl;

import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dao.MarksDao;
import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DaoJdbcTest {
    private Connection connection;
    private MarksDao marksDao;
    private InternDao internDao;

    @BeforeEach
    void setUp() throws Exception {
        JdbcConnectionPool cp = JdbcConnectionPool.create(
                "jdbc:h2:mem:testdb", "sa", "");
        connection = cp.getConnection();
        marksDao = new MarksDaoJdbc(connection);
        internDao = new InternDaoJdbc(connection, marksDao);
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
        List<InternEntity> interns = internDao.readAll();

        assertEquals(1, interns.size());
        assertEquals("Ivan", interns.get(0).getFirstName());
        assertEquals("Ivanov", interns.get(0).getLastName());
    }

    @Test
    void testCreateIntern() {
        InternEntity newIntern = new InternEntity(null, "Ivan", "Ivanov", List.of());
        InternEntity createdIntern = internDao.create(newIntern);

        assertNotNull(createdIntern.getId());
        assertEquals("Ivan", createdIntern.getFirstName());
        assertEquals("Ivanov", createdIntern.getLastName());
    }

    @Test
    void testReadById() {
        InternEntity newIntern = new InternEntity(null, "Petr", "Petrov", List.of());
        InternEntity createdIntern = internDao.create(newIntern);

        InternEntity internEntity = internDao.readById(createdIntern.getId());

        assertNotNull(internEntity);
        assertEquals("Petr", internEntity.getFirstName());
        assertEquals("Petrov", internEntity.getLastName());
    }

    @Test
    void testUpdateMarks() {
        InternEntity newIntern = new InternEntity(null, "Sergey", "Sergeev", List.of());
        InternEntity createdIntern = internDao.create(newIntern);

        MarkEntity newMark = new MarkEntity(null, "SQL", 5, createdIntern.getId());
        MarkEntity markEntity = marksDao.updateInternMarks(newMark);

        assertNotNull(markEntity);
        assertEquals("SQL", markEntity.getSubject());
        assertEquals(5, markEntity.getMark());
    }

    @Test
    void testDeleteIntern() {
        InternEntity newIntern = new InternEntity(null, "Andrey", "Andreev", List.of());
        InternEntity createdIntern = internDao.create(newIntern);

        Boolean isDeleted = internDao.delete(createdIntern.getId());
        assertTrue(isDeleted);

        InternEntity deletedIntern = internDao.readById(createdIntern.getId());
        assertNull(deletedIntern);
    }
}
