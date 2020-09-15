package br.com.zup.bootcamp.proposta.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocaleMessageSource {

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");
    private final MessageSource messageSource;

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(DEFAULT_LOCALE);
        return slr;
    }

    public String obterMensagem(String mensagem) {
        return messageSource.getMessage(mensagem, null, DEFAULT_LOCALE);
    }

    public String obterMensagem(String mensagem, Object... parametros) {
        return messageSource.getMessage(mensagem, parametros, DEFAULT_LOCALE);
    }

    public String obterMensagem(FieldError fieldError) {
        return messageSource.getMessage(fieldError, DEFAULT_LOCALE);
    }
}
