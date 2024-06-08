package fr.clementjaminion.macaronsbackend.exceptions;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nullable HttpHeaders headers, @Nullable HttpStatusCode status, @Nullable WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MacaronNotFoundException.class)
    public ResponseEntity<Object> handleMacaronNotFoundException(MacaronNotFoundException ex) {
        return new ResponseEntity<>(Map.of("code", ex.getCode(), "message", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MacaronsFunctionalException.class)
    public ResponseEntity<Object> handleMacaronsFunctionalException(MacaronsFunctionalException ex) {
        return new ResponseEntity<>(Map.of("code", ex.getCode(), "message", ex.getMessage()), HttpStatus.CONFLICT);
    }
}
