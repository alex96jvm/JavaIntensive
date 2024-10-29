package dev.alex96jvm.javaintensive.dao;

import dev.alex96jvm.javaintensive.model.MarkEntity;
import java.util.List;

public interface MarksDao {
    MarkEntity updateInternMarks(MarkEntity markEntity);

    List<MarkEntity> getMarksByInternId(Long internId);
}
