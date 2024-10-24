package dev.alex96jvm.javaintensive.dao;

import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import java.util.List;
import java.util.Optional;

public interface InternDao {
    List<InternEntity> readAll();

    Optional<InternEntity> readById(Long id);

    InternEntity create(InternEntity internEntity);

    Optional<InternEntity> update(MarkEntity markEntity);

    Boolean delete(Long id);
}
