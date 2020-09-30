package br.com.zup.bootcamp.proposta.proxy;

import br.com.zup.bootcamp.proposta.dto.AnaliseRequest;
import br.com.zup.bootcamp.proposta.dto.AnaliseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "analise-service", url = "${analise.base-url}")
public interface AnaliseProxy {

    @PostMapping("/solicitacao")
    AnaliseResponse analisar(AnaliseRequest analiseRequest);
}
