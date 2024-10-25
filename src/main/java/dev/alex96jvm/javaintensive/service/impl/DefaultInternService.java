package dev.alex96jvm.javaintensive.service.impl;

import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dao.MarksDao;
import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import dev.alex96jvm.javaintensive.mapper.InternMapper;
import dev.alex96jvm.javaintensive.mapper.MarkMapper;
import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import dev.alex96jvm.javaintensive.service.InternService;
import dev.alex96jvm.javaintensive.validation.Validator;
import java.util.List;
import java.util.Optional;

public class DefaultInternService implements InternService {
    private final InternDao internDao;
    private final MarksDao marksDao;
    private final Validator validator;

    public DefaultInternService(InternDao internDao, MarksDao marksDao) {
        this.marksDao = marksDao;
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
    public Optional<InternDto> getInternById(Long id) throws InternException {
        validator.validateId(id);
        return Optional.ofNullable(internDao.readById(id))
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
        MarkEntity markEntity = marksDao.updateInternMarks(MarkMapper.INSTANCE.markDtoToMarkEntity(markDto));
        InternEntity internEntity = internDao.readById(markEntity.getInternId());
        return Optional.ofNullable(internEntity)
                .map(this::mapToInternDto);
    }

    @Override
    public Boolean deleteIntern(Long id) throws InternException {
        validator.validateId(id);
        return internDao.delete(id);
    }

    private InternDto mapToInternDto(InternEntity internEntity) {
        return InternMapper.INSTANCE.internEntityToInternDto(internEntity);
    }
}
