package dev.alex96jvm.javaintensive.mapper;

import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarkMapper {
    MarkMapper INSTANCE = Mappers.getMapper(MarkMapper.class);

    MarkEntity markDtoToMarkEntity(MarkDto markDto);
}
