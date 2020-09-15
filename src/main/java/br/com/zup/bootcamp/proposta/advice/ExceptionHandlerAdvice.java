package br.com.zup.bootcamp.proposta.advice;

import br.com.zup.bootcamp.proposta.config.LocaleMessageSource;
import br.com.zup.bootcamp.proposta.dto.ErroApi;
import br.com.zup.bootcamp.proposta.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

    private final LocaleMessageSource localeMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        String mensagem = localeMessageSource.obterMensagem("erro-validacao");
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem);
        erroApi.adicionarBindingResult(methodArgumentNotValidException.getBindingResult(), localeMessageSource);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroApi);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroApi> manipuladorApiException(ApiException ex) {
        String mensagem = localeMessageSource.obterMensagem(ex.getMessage(), ex.getParams().toArray());
        ErroApi erroApi = new ErroApi(ex.getStatus(), mensagem);
        return ResponseEntity.status(ex.getStatus()).body(erroApi);
    }
}
