package dev.alex96jvm.javaintensive.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.service.InternService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerServletTest {
    @Mock
    private InternService internService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private ControllerServlet controllerServlet;
    private ObjectMapper objectMapper;
    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testGetAllInterns() throws Exception {
        InternDto internDto = new InternDto();
        internDto.setId(1L);
        internDto.setFirstName("Ivan");
        internDto.setLastName("Petrov");
        List<InternDto> internList = List.of(internDto);
        when(internService.getAllInterns()).thenReturn(internList);
        when(request.getMethod()).thenReturn("GET");

        controllerServlet.service(request, response);

        verify(internService, times(1)).getAllInterns();
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        String expectedJson = objectMapper.writeValueAsString(internList);
        assert(responseWriter.toString().contains(expectedJson));
    }

    @Test
    public void testGetInternById() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        InternDto internDto = new InternDto();
        internDto.setId(1L);
        internDto.setFirstName("Ivan");
        internDto.setLastName("Petrov");
        when(internService.getInternById(1L)).thenReturn(Optional.of(internDto));
        when(request.getMethod()).thenReturn("GET");

        controllerServlet.service(request, response);

        verify(internService, times(1)).getInternById(1L);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        String expectedJson = objectMapper.writeValueAsString(internDto);
        assert(responseWriter.toString().contains(expectedJson));
    }

    @Test
    public void testPostCreateIntern() throws Exception {
        when(request.getPathInfo()).thenReturn("/");
        String internJson = "{\"firstName\":\"Ivan\",\"lastName\":\"Petrov\"}";
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(internJson)));
        InternDto internDto = new InternDto();
        internDto.setFirstName("Ivan");
        internDto.setLastName("Petrov");
        when(internService.createIntern(any(InternDto.class))).thenReturn(internDto);
        when(request.getMethod()).thenReturn("POST");

        controllerServlet.service(request, response);

        verify(internService, times(1)).createIntern(any(InternDto.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String expectedJson = objectMapper.writeValueAsString(internDto);
        assert(responseWriter.toString().contains(expectedJson));
    }

    @Test
    public void testUpdateInternMarksThrowsIOException() throws Exception {
        when(request.getPathInfo()).thenReturn("/marks");
        when(request.getReader()).thenThrow(new IOException());

        controllerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("Invalid request", responseWriter.toString());
    }

    @Test
    public void testDeleteIntern() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        when(internService.deleteIntern(1L)).thenReturn(true);
        when(request.getMethod()).thenReturn("DELETE");

        controllerServlet.service(request, response);

        verify(internService, times(1)).deleteIntern(1L);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDeleteInternButNotFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/999");
        when(internService.deleteIntern(999L)).thenReturn(false);
        when(request.getMethod()).thenReturn("DELETE");

        controllerServlet.service(request, response);

        verify(internService, times(1)).deleteIntern(999L);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assert(responseWriter.toString().contains("Intern not found"));
    }
}
