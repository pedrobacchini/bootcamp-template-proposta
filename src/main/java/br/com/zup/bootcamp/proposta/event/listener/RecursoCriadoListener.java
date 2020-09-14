package br.com.zup.bootcamp.proposta.event.listener;

import br.com.zup.bootcamp.proposta.event.RecursoCriadoEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

    @Override
    public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
        HttpServletResponse resposta = recursoCriadoEvent.getResposta();
        String idString = recursoCriadoEvent.getId().toString();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(idString).toUri();
        resposta.setHeader("Location", uri.toASCIIString());
    }
}
