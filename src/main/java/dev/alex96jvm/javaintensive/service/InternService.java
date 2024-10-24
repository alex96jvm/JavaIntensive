package dev.alex96jvm.javaintensive.service;

import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import java.util.List;
import java.util.Optional;


public interface InternService {
    List<InternDto> getAllInterns();

    Optional<InternDto> getInternById(Long id);

    InternDto createIntern(InternDto internDto) throws InternException;

    Optional<InternDto> updateInternMarks(MarkDto markDto) throws InternException;

    Boolean deleteIntern(Long id);
}
