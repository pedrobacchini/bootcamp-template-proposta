package br.com.zup.bootcamp.proposta.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "analise-service", url = "${analise.base-url}")
public interface AnaliseFinanceiraProxy {

    @PostMapping("/solicitacao")
    RespostaAnaliseFinanceira analisar(SolicitacaoAnaliseFinanceira solicitacaoAnaliseFinanceira);
}
