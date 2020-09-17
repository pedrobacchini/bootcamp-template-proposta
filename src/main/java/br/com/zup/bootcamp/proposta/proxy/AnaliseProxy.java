package br.com.zup.bootcamp.proposta.proxy;

import br.com.zup.bootcamp.proposta.dto.AnaliseRequest;
import br.com.zup.bootcamp.proposta.dto.AnaliseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analise-service", url = "${analise.base-url}", decode404 = true)
public interface AnaliseProxy {

    @RequestMapping(method = RequestMethod.POST, value = "/solicitacao")
    AnaliseResponse analisar(AnaliseRequest analiseRequest);
}
