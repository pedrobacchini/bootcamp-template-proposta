package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;

public interface AnaliseFinanceiraService {

    StatusAnalise analisar(Proposta proposta);
}
