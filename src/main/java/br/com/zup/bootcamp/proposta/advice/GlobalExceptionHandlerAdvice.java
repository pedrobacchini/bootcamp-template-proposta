package br.com.zup.bootcamp.proposta.advice;

import br.com.zup.bootcamp.proposta.config.LocaleMessageSource;
import br.com.zup.bootcamp.proposta.dto.ErroApi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final LocaleMessageSource localeMessageSource;

    @Override
    @NotNull
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         @NotNull HttpHeaders headers,
                                                         @NotNull HttpStatus status,
                                                         @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("erro-validacao");
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem);
        erroApi.adicionarBindingResult(ex.getBindingResult(), localeMessageSource);
        return handleExceptionInternal(ex, erroApi, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("erro-validacao");
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem);
        erroApi.adicionarBindingResult(ex.getBindingResult(), localeMessageSource);
        return handleExceptionInternal(ex, erroApi, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("json-formatacao-invalida");
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NotNull MissingServletRequestParameterException ex,
                                                                          @NotNull HttpHeaders headers,
                                                                          @NotNull HttpStatus status,
                                                                          @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("parametro-ausente", ex.getParameterName());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(status, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, status, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleMissingServletRequestPart(@NotNull MissingServletRequestPartException ex,
                                                                     @NotNull HttpHeaders headers,
                                                                     @NotNull HttpStatus status,
                                                                     @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("parametro-ausente", ex.getRequestPartName());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(status, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, status, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NotNull HttpRequestMethodNotSupportedException ex,
                                                                         @NotNull HttpHeaders headers,
                                                                         @NotNull HttpStatus status,
                                                                         @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("metodo-nao-suportado", ex.getMethod());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, status, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     @NotNull HttpHeaders headers,
                                                                     @NotNull HttpStatus status,
                                                                     @NotNull WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        String tipoEsperado = builder.substring(0, builder.length() - 2);
        String mensagem = localeMessageSource.obterMensagem("tipo-midia-nao-suportado", ex.getContentType(), tipoEsperado);
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(HttpStatus.UNSUPPORTED_MEDIA_TYPE, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, status, request);
    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleServletRequestBindingException(@NotNull ServletRequestBindingException ex,
                                                                          @NotNull HttpHeaders headers,
                                                                          @NotNull HttpStatus status,
                                                                          @NotNull WebRequest request) {
        String mensagem = localeMessageSource.obterMensagem("solicitacao-ilegal");
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        ErroApi erroApi = new ErroApi(HttpStatus.BAD_REQUEST, mensagem, mensagemDesenvolvedor);
        return super.handleExceptionInternal(ex, erroApi, headers, status, request);
    }
}
