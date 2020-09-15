package br.com.zup.bootcamp.proposta.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ApiException extends RuntimeException {

    private static final long serialVersionUID = -1135216598830903614L;

    protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    protected final List<Object> params = new ArrayList<>();

    ApiException(String message) { super(message); }
}
