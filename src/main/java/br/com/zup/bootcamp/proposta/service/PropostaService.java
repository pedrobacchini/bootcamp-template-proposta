package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;

public interface PropostaService {

    Proposta criar(PropostaInput propostaInput);
}
