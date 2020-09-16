package br.com.zup.bootcamp.proposta.resource;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.service.PropostaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class PropostaResource {

    public static final String ENDPOINT_PATH = "/propostas";

    private final PropostaService propostaService;

    @PostMapping(path = ENDPOINT_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> criar(@Valid @RequestBody PropostaInput propostaInput) throws URISyntaxException {
        Proposta proposta = propostaService.criar(propostaInput);
        return ResponseEntity
                .created(new URI(String.format("%s/%s", ENDPOINT_PATH, proposta.getId())))
                .build();
    }
}
