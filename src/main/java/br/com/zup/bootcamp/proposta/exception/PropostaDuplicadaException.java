package br.com.zup.bootcamp.proposta.exception;

import org.springframework.http.HttpStatus;

public class PropostaDuplicadaException extends ApiException {

    private static final long serialVersionUID = -1772320525171757808L;

    public PropostaDuplicadaException() {
        super("proposta-ja-cadastrada");
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
