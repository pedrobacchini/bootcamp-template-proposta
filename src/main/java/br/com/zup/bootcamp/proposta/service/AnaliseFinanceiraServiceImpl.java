package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import br.com.zup.bootcamp.proposta.integration.RespostaAnaliseFinanceira;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.integration.AnaliseFinanceiraProxy;
import br.com.zup.bootcamp.proposta.integration.SolicitacaoAnaliseFinanceira;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnaliseFinanceiraServiceImpl implements AnaliseFinanceiraService {

    private final AnaliseFinanceiraProxy analiseFinanceiraProxy;

    @Override
    public StatusAnalise analisar(Proposta proposta) {
        try {
            log.info("Solicitando analise da proposta id: {} cliente: {}", proposta.getId(), proposta.getNome());
            RespostaAnaliseFinanceira resposta = analiseFinanceiraProxy.analisar(SolicitacaoAnaliseFinanceira.fromProposta(proposta));
            log.info("Analise concluida com o status: {}", resposta.getResultadoSolicitacao());
            return resposta.getResultadoSolicitacao();
        } catch (FeignException.FeignClientException ex) {
            log.info("Erro ao tentar fazer a analise da proposta {}", ex.getMessage());
            throw ex;
        }
    }
}
