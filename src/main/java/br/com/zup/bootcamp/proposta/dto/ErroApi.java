package br.com.zup.bootcamp.proposta.dto;

import br.com.zup.bootcamp.proposta.config.LocaleMessageSource;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErroApi {

    HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    final LocalDateTime timestamp;
    String mensagem;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String mensagemDesenvolvedor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<SubErroApi> erros;

    private ErroApi() { this.timestamp = LocalDateTime.now(); }

    public ErroApi(HttpStatus status, String mensagem) {
        this();
        this.status = status;
        this.mensagem = mensagem;
    }

    public ErroApi(HttpStatus status, String mensagem, String mensagemDesenvolvedor) {
        this();
        this.status = status;
        this.mensagem = mensagem;
        this.mensagemDesenvolvedor = mensagemDesenvolvedor;
    }

    public void adicionarBindingResult(BindingResult bindingResult, LocaleMessageSource localeMessageSource) {
        bindingResult.getGlobalErrors().forEach(this::adicionarErroValidacao);
        bindingResult.getFieldErrors().forEach(fieldError -> adicionarErroValidacao(fieldError, localeMessageSource));
    }

    private void adicionarErroValidacao(ObjectError objectError) {
        this.adicionarErroValidacao(objectError.getObjectName(), objectError.getDefaultMessage());
    }

    private void adicionarErroValidacao(FieldError fieldError, LocaleMessageSource localeMessageSource) {
        this.adicionarErroValidacao(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                localeMessageSource.obterMensagem(fieldError));
    }

    private void adicionarErroValidacao(String nome, String campo, Object valorRejeitado, String erro) {
        adicionarSubErro(new ErroValidacaoApi(nome, campo, valorRejeitado, erro));
    }

    public void adicionarErroValidacao(String nome, String erro) {
        adicionarSubErro(new ErroValidacaoApi(nome, erro));
    }

    private void adicionarSubErro(SubErroApi subErroApi) {
        if (erros == null) {
            erros = new ArrayList<>();
        }
        erros.add(subErroApi);
    }

    @JsonDeserialize(as = ErroValidacaoApi.class)
    abstract static class SubErroApi {
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class ErroValidacaoApi extends SubErroApi {
        final String objeto;
        String campo;
        Object valorRejeitado;
        final String erro;

        ErroValidacaoApi(String nome, String erro) {
            this.objeto = nome;
            this.erro = erro;
        }
    }
}

