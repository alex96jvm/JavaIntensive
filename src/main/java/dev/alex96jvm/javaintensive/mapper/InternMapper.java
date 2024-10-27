package dev.alex96jvm.javaintensive.mapper;

import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.model.InternEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InternMapper {
    InternMapper INSTANCE = Mappers.getMapper(InternMapper.class);

    InternDto internEntityToInternDto(InternEntity internEntity);

    InternEntity internDtoToInternEntity(InternDto internDto);
}
