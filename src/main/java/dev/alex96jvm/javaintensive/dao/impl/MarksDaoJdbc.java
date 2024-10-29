package dev.alex96jvm.javaintensive.dao.impl;

import dev.alex96jvm.javaintensive.dao.MarksDao;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MarksDaoJdbc implements MarksDao {
    private final Logger logger;
    private final Connection connection;

    public MarksDaoJdbc(Connection connection) {
        this.connection = connection;
        logger = Logger.getLogger(InternDaoJdbc.class.getName());
    }

    @Override
    public MarkEntity updateInternMarks(MarkEntity markEntity) {
        String query = "INSERT INTO marks (subject, mark, intern_id) VALUES (?, ?, ?) ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, markEntity.getSubject());
            statement.setInt(2, markEntity.getMark());
            statement.setLong(3, markEntity.getInternId());
            statement.execute();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return markEntity;
    }

    @Override
    public List<MarkEntity> getMarksByInternId(Long internId) {
        String query = "SELECT * FROM marks WHERE intern_id = ?";
        List<MarkEntity> marks = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, internId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long markId = resultSet.getLong("id");
                String subject = resultSet.getString("subject");
                int mark = resultSet.getInt("mark");
                marks.add(new MarkEntity(markId, subject, mark, internId));
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return marks;
    }
}
