package br.com.zup.bootcamp.proposta.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

@Getter
public class RecursoCriadoEvent extends ApplicationEvent {

    private final HttpServletResponse resposta;
    private final Long id;

    public RecursoCriadoEvent(Object source, HttpServletResponse resposta, Long id) {
        super(source);
        this.resposta = resposta;
        this.id = id;
    }
}
