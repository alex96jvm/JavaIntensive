package dev.alex96jvm.javaintensive.service.impl;

import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import dev.alex96jvm.javaintensive.model.InternEntity;
import dev.alex96jvm.javaintensive.model.MarkEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultInternServiceTest {
    @InjectMocks
    private DefaultInternService internService;
    @Mock
    private InternDao internDao;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInterns() {
        InternEntity internEntity1 = new InternEntity(1L, "Ivan", "Ivanov", Collections.emptyList());
        InternEntity internEntity2 = new InternEntity(2L, "Petr", "Petrov", Collections.emptyList());
        Mockito.when(internDao.readAll()).thenReturn(List.of(internEntity1, internEntity2));

        List<InternDto> interns = internService.getAllInterns();

        assertEquals(2, interns.size());
        assertEquals("Ivan", interns.get(0).getFirstName());
        assertEquals("Petrov", interns.get(1).getLastName());
        verify(internDao, times(1)).readAll();
    }

    @Test
    void testGetInternById() {
        InternEntity internEntity = new InternEntity(5L, "Ivan", "Ivanov", Collections.emptyList());
        when(internDao.readById(5L)).thenReturn(Optional.of(internEntity));

        Optional<InternDto> internDto = internService.getInternById(5L);

        assertTrue(internDto.isPresent());
        assertEquals("Ivan", internDto.get().getFirstName());
        assertEquals("Ivanov", internDto.get().getLastName());
        verify(internDao, times(1)).readById(5L);
    }

    @Test
    void testCreateIntern() throws InternException {
        InternDto internDto = new InternDto();
        internDto.setFirstName("Oleg");
        internDto.setLastName("Semin");
        InternEntity internEntity = new InternEntity(3L, "Oleg", "Semin", Collections.emptyList());
        when(internDao.create(any(InternEntity.class))).thenReturn(internEntity);

        InternDto createdIntern = internService.createIntern(internDto);

        assertEquals("Oleg", createdIntern.getFirstName());
        assertEquals("Semin", createdIntern.getLastName());
        verify(internDao, times(1)).create(any(InternEntity.class));
    }

    @Test
    void testDeleteIntern() {
        when(internDao.delete(10L)).thenReturn(true);

        Boolean deleted = internService.deleteIntern(10L);

        assertTrue(deleted);
        verify(internDao, times(1)).delete(10L);
    }

    @Test
    void testUpdateInternMarks() throws InternException {
        MarkDto markDto = new MarkDto();
        markDto.setSubject("Java Core");
        markDto.setMark(3);
        markDto.setInternId(4L);
        InternEntity internEntity = new InternEntity(3L, "Oleg", "Semin",
                List.of(new MarkEntity(1L, "Java Core", 5, 3L)));
        when(internDao.update(any(MarkEntity.class))).thenReturn(Optional.of(internEntity));

        Optional<InternDto> updatedIntern = internService.updateInternMarks(markDto);
        assertNotNull(updatedIntern.orElseThrow().getMarks());
        assertInstanceOf(List.class, updatedIntern.orElseThrow().getMarks());
        verify(internDao, times(1)).update(any(MarkEntity.class));
    }
}

