package dev.alex96jvm.javaintensive.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alex96jvm.javaintensive.dao.InternDao;
import dev.alex96jvm.javaintensive.dao.impl.DatabaseConnection;
import dev.alex96jvm.javaintensive.dao.impl.InternDaoJdbc;
import dev.alex96jvm.javaintensive.dto.InternDto;
import dev.alex96jvm.javaintensive.dto.MarkDto;
import dev.alex96jvm.javaintensive.exception.InternException;
import dev.alex96jvm.javaintensive.service.InternService;
import dev.alex96jvm.javaintensive.service.impl.DefaultInternService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
/**
 * Контроллер-сервлет для обработки HTTP-запросов, связанных с управлением стажерами.
 * Предоставляет REST API для создания, получения, обновления и удаления стажеров.
 * <p>
 * Поддерживаемые маршруты:
 * <p>
 * - GET /v1/interns - получить список всех стажеров.
 * <p>
 * - GET /v1/interns/{id} - получить конкретного стажера по его ID.
 * <p>
 * - POST /v1/interns - добавить нового стажера.
 * <p>
 * - POST /v1/interns/marks - выставить оценку стажеру.
 * <p>
 * - DELETE /v1/interns/{id} - удалить стажера по ID.
 */
@WebServlet("/v1/interns/*")
public class ControllerServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private InternService internService;

    public ControllerServlet() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void init() {
        InternDao internDao = new InternDaoJdbc(DatabaseConnection.getConnection());
        this.internService = new DefaultInternService(internDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<InternDto> interns = internService.getAllInterns();
            writeResponseAsJson(resp, interns);
        } else if (pathInfo.matches("/\\d{1,10}")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            Optional<InternDto> internOpt = internService.getInternById(id);
            writeInternResponse(internOpt, resp);
        } else {
            writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                InternDto internDto = objectMapper.readValue(req.getReader(), InternDto.class);
                InternDto createdIntern = internService.createIntern(internDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writeResponseAsJson(resp, createdIntern);
            } else if (pathInfo.equals("/marks")) {
                MarkDto markDto = objectMapper.readValue(req.getReader(), MarkDto.class);
                Optional<InternDto> updatedInternOpt = internService.updateInternMarks(markDto);
                writeInternResponse(updatedInternOpt, resp);
            }
        } catch (IOException ioException) {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        } catch (InternException internException) {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, internException.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.matches("/\\d{1,10}")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean deleted = internService.deleteIntern(id);
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Intern not found");
            }
        } else {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "ID is required for deletion");
        }
    }

    private void writeResponseAsJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), object);
    }

    private void writeInternResponse(Optional<InternDto> internOpt, HttpServletResponse resp) throws IOException {
        if (internOpt.isPresent()) {
            writeResponseAsJson(resp, internOpt.get());
        } else {
            writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Intern not found");
        }
    }

    private void writeErrorResponse(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(message);
    }
}
