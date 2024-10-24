package dev.alex96jvm.javaintensive.validation;

import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void testValidateInternDtoThrowsExceptionWhenFirstnameTooShort() {
        InternDto internDto = new InternDto();
        internDto.setFirstName("A");
        internDto.setLastName("Ivanov");

        InternException exception = assertThrows(InternException.class, () -> validator.validateInternDto(internDto));

        assertEquals("Firstname can not be less than 2 and more than 30 symbols", exception.getMessage());
    }

    @Test
    void testValidateInternDtoThrowsExceptionWhenLastnameTooLong() {
        InternDto internDto = new InternDto();
        internDto.setFirstName("Ivan");
        internDto.setLastName("I".repeat(31));

        InternException exception = assertThrows(InternException.class, () -> validator.validateInternDto(internDto));

        assertEquals("Lastname can not be less than 2 and more than 30 symbols", exception.getMessage());
    }

    @Test
    void testValidateMarkDtoThrowsExceptionWhenMarkOutOfRange() {
        MarkDto markDto = new MarkDto();
        markDto.setSubject("Math");
        markDto.setMark(6);

        InternException exception = assertThrows(InternException.class, () -> validator.validateMarkDto(markDto));

        assertEquals("Mark must be in the range from 1 to 5", exception.getMessage());
    }

    @Test
    void testValidateMarkDtoThrowsExceptionWhenSubjectTooShort() {
        MarkDto markDto = new MarkDto();
        markDto.setSubject("M");
        markDto.setMark(4);

        InternException exception = assertThrows(InternException.class, () -> validator.validateMarkDto(markDto));

        assertEquals("Subject can not be less than 2 and more than 30 symbols", exception.getMessage());
    }
}
