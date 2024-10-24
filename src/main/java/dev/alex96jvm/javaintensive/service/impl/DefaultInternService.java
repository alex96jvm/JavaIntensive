package dev.alex96jvm.javaintensive.service.impl;

import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import dev.alex96jvm.javaintensive.mapper.InternMapper;
import dev.alex96jvm.javaintensive.mapper.MarkMapper;
import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.service.InternService;
import dev.alex96jvm.javaintensive.validation.Validator;
import java.util.List;
import java.util.Optional;

public class DefaultInternService implements InternService {
    private final InternDao internDao;
    private final Validator validator;

    public DefaultInternService(InternDao internDao) {
        this.internDao = internDao;
        this.validator = new Validator();
    }

    @Override
    public List<InternDto> getAllInterns() {
        return internDao.readAll()
                .stream().map(InternMapper.INSTANCE::internEntityToInternDto)
                .toList();
    }

    @Override
    public Optional<InternDto> getInternById(Long id) {
        return internDao.readById(id)
                .map(this::mapToInternDto);
    }

    @Override
    public InternDto createIntern(InternDto internDto) throws InternException {
        validator.validateInternDto(internDto);
        InternEntity internEntity = internDao.create(InternMapper.INSTANCE.internDtoToInternEntity(internDto));
        return mapToInternDto(internEntity);
    }

    @Override
    public Optional<InternDto> updateInternMarks(MarkDto markDto) throws InternException {
        validator.validateMarkDto(markDto);
        return internDao.update(MarkMapper.INSTANCE.markDtoToMarkEntity(markDto))
                .map(this::mapToInternDto);
    }

    @Override
    public Boolean deleteIntern(Long id) {
        return internDao.delete(id);
    }

    private InternDto mapToInternDto(InternEntity internEntity) {
        return InternMapper.INSTANCE.internEntityToInternDto(internEntity);
    }
}
