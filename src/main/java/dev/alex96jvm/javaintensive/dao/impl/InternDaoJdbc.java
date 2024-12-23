package dev.alex96jvm.javaintensive.dao.impl;

import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dao.MarksDao;
import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InternDaoJdbc implements InternDao {
    private final Connection connection;
    private final MarksDao marksDao;
    private final Logger logger;

    public InternDaoJdbc(Connection connection, MarksDao marksDao) {
        this.connection = connection;
        this.marksDao = marksDao;
        logger = Logger.getLogger(InternDaoJdbc.class.getName());
    }

    @Override
    public List<InternEntity> readAll() {
        List<InternEntity> interns = new ArrayList<>();
        String query = "SELECT * FROM interns";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                interns.add(new InternEntity(
                        id,
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        marksDao.getMarksByInternId(id)
                ));
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return interns;
    }

    @Override
    public InternEntity readById(Long id) {
        InternEntity internEntity = getInternDetailsById(id).orElse(null);
        if (internEntity != null) {
            List<MarkEntity> markEntities = marksDao.getMarksByInternId(id);
            internEntity.setMarks(markEntities);
        }
        return internEntity;
    }

    public InternEntity create(InternEntity internEntity) {
        String query = "INSERT INTO interns (firstname, lastname) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, internEntity.getFirstName());
            statement.setString(2, internEntity.getLastName());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                internEntity.setId(generatedKeys.getLong(1));
            }
            generatedKeys.close();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return internEntity;
    }

    @Override
    public Boolean delete(Long id) {
        String query = "DELETE FROM interns WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            return false;
        }
    }

    private Optional<InternEntity> getInternDetailsById(Long id) {
        String query = "SELECT * FROM interns WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long internId = resultSet.getLong("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                return Optional.of(new InternEntity(internId, firstname, lastname, new ArrayList<>()));
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }
}
