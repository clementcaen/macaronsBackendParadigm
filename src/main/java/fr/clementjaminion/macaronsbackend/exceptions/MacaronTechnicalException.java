package fr.clementjaminion.macaronsbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MacaronTechnicalException extends Exception {
    private final String code;

    public MacaronTechnicalException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacaronTechnicalException exception = (MacaronTechnicalException) o;
        return Objects.equals(code, exception.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
