package com.surrogate.numy.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 🔹 1. Error genérico — fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("Error no manejado", e);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e.getMessage());
    }

    // 🔹 2. Errores de JSON inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = (ex.getCause() instanceof MismatchedInputException)
                ? "El cuerpo de la solicitud debe ser un JSON válido o bien formado."
                : "Error en el formato de entrada: " + ex.getMessage();
        return buildError(HttpStatus.BAD_REQUEST, message, null);
    }

    // 🔹 3. Método HTTP no soportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildError(HttpStatus.METHOD_NOT_ALLOWED,
                "Método HTTP no soportado: " + ex.getMethod(),
                "Métodos permitidos: " + ex.getSupportedHttpMethods());
    }

    // 🔹 4. Tipo de contenido inválido
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return buildError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Tipo de contenido no soportado. Usa 'application/json'.",
                ex.getContentType() != null ? ex.getContentType().toString() : null);
    }

    // 🔹 5. Argumentos inválidos (ej: parámetros incorrectos)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // 🔹 6. Validaciones fallidas (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        return buildError(HttpStatus.BAD_REQUEST, "Error de validación", errores);
    }

    // 🔹 7. Falta de parámetros obligatorios en request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return buildError(HttpStatus.BAD_REQUEST,
                "Falta un parámetro obligatorio: " + ex.getParameterName(), null);
    }

    // 🔹 8. Tipo incorrecto de parámetro
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildError(HttpStatus.BAD_REQUEST,
                "Tipo de parámetro incorrecto: '" + ex.getName() + "' debe ser de tipo " + ex.getRequiredType(),
                null);
    }

    // 🔹 9. No se encontró un endpoint
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Endpoint no encontrado", ex.getRequestURL());
    }

    // 🔹 10. Entidad no encontrada (JPA)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Entidad no encontrada", ex.getMessage());
    }

    // 🔹 11. Sin resultados (Query sin resultado)
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Map<String, Object>> handleNoResult(NoResultException ex) {
        return buildError(HttpStatus.NOT_FOUND, "No se encontraron resultados", ex.getMessage());
    }

    // 🔹 12. Violaciones de constraints de base de datos (Hibernate/JPA)
    @ExceptionHandler({
            ConstraintViolationException.class,
            jakarta.validation.ConstraintViolationException.class,
            SQLIntegrityConstraintViolationException.class,
            DataIntegrityViolationException.class,
            PropertyValueException.class
    })
    public ResponseEntity<Map<String, Object>> handleConstraintViolations(Exception ex) {
        String msg = "Violación de integridad de datos o constraint";
        if (ex.getMessage() != null && ex.getMessage().contains("Duplicate"))
            msg = "El registro ya existe o viola una restricción única.";
        return buildError(HttpStatus.CONFLICT, msg, ex.getMessage());
    }


    // 🔹 14. Errores de conexión con la BD
    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<Map<String, Object>> handleJdbcConnection(CannotGetJdbcConnectionException ex) {
        return buildError(HttpStatus.SERVICE_UNAVAILABLE,
                "No se pudo conectar con la base de datos.",
                ex.getMostSpecificCause().getMessage());
    }

    // 🔹 15. Errores generales de Hibernate
    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<Map<String, Object>> handleHibernate(HibernateException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno de Hibernate", ex.getMessage());
    }

    // 🔹 16. Errores de Spring Data
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccess(DataAccessException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al acceder a los datos", ex.getMostSpecificCause().getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensaje, Object detalle) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", mensaje);
        if (detalle != null) body.put("detalle", detalle);
        return ResponseEntity.status(status).body(body);
    }
}