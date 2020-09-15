package br.com.zup.bootcamp.proposta.advice;

import br.com.zup.bootcamp.proposta.config.LocaleMessageSource;
import br.com.zup.bootcamp.proposta.dto.ErroApi;
import br.com.zup.bootcamp.proposta.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    private final LocaleMessageSource localeMessageSource;

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> manipuladorApiException(ApiException ex, WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem(ex.getMessage(), ex.getParams().toArray());
        ErroApi erroApi = new ErroApi(ex.getStatus(), mensagem);
        return handleExceptionInternal(ex, erroApi, new HttpHeaders(), ex.getStatus(), request);
    }
}
