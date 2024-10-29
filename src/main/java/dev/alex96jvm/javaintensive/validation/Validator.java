package dev.alex96jvm.javaintensive.validation;

import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.*;

public class Validator {
    public void validateInternDto(InternDto internDto) throws InternException {
        validateLength(internDto.getFirstName(), "Firstname");
        validateLength(internDto.getLastName(), "Lastname");
    }

    public void validateMarkDto(MarkDto markDto) throws InternException {
        validateLength(markDto.getSubject(), "Subject");
        if (markDto.getMark() < 1 || markDto.getMark() > 5) {
            throw new InternException("Mark must be in the range from 1 to 5");
        }
    }

    public void validateId(Long id) throws InternException {
        if (id == null || id <= 0) {
            throw new InternException("Id must be a positive number");
        }
    }

    private void validateLength(String data, String value) throws InternException {
        if (data.length() < 2 || data.length() > 30) {
            throw new InternException(String.format("%s can not be less than 2 and more than 30 symbols", value));
        }
    }
}
