package br.com.zup.bootcamp.proposta.resource;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.event.RecursoCriadoEvent;
import br.com.zup.bootcamp.proposta.service.PropostaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/propostas")
@RequiredArgsConstructor
public class PropostaResource {

    private final PropostaService propostaService;
    private final ApplicationEventPublisher publicador;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void criar(@Valid @RequestBody PropostaInput propostaInput, HttpServletResponse resposta) {
        Proposta proposta = propostaService.criar(propostaInput);
        publicador.publishEvent(new RecursoCriadoEvent(this, resposta, proposta.getId()));
    }
}
