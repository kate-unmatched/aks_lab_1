package org.geodispatch.mappers;

import jakarta.ejb.EJBException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import org.geodispatch.utils.ErrorDto;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Exception> {

    @Override public Response toResponse(Exception ex) {
        Throwable root = unwrap(ex);

        // 1) Bean Validation → 400 + поля
        if (root instanceof ConstraintViolationException cve) {
            Map<String,String> fields = new LinkedHashMap<>();
            for (ConstraintViolation<?> v : cve.getConstraintViolations()) {
                fields.put(v.getPropertyPath().toString(), v.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ErrorDto.of("VALIDATION_ERROR", "Validation failed", fields))
                    .build();
        }

        // 2) 404
        if (root instanceof NotFoundException || root instanceof java.util.NoSuchElementException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ErrorDto.of("NOT_FOUND", root.getMessage()))
                    .build();
        }

        // 3) 400
        if (root instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ErrorDto.of("BAD_REQUEST", root.getMessage()))
                    .build();
        }

        // 4) Конфликты/БД
        if (root instanceof PersistenceException pe) {
            String msg = pe.getMessage();
            boolean unique = msg != null && msg.toLowerCase().contains("unique");
            return Response.status(unique ? Response.Status.CONFLICT : Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ErrorDto.of(unique ? "CONFLICT" : "PERSISTENCE_ERROR",
                            unique ? "Unique constraint violation" : "Database error"))
                    .build();
        }

        // 5) Всё остальное → 500
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErrorDto.of("INTERNAL_ERROR", root.getMessage() != null ? root.getMessage() : "Unexpected error"))
                .build();
    }

    private static Throwable unwrap(Throwable t) {
        if (t instanceof EJBException ejb && ejb.getCause() != null) return unwrap(ejb.getCause());
        if (t.getCause() != null && t.getCause() != t) return unwrap(t.getCause());
        return t;
    }
}
