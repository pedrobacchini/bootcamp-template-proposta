package br.com.zup.bootcamp.proposta.integration;

import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RespostaAnaliseFinanceira {

    String documento;

    String nome;

    String idProposta;

    StatusAnalise resultadoSolicitacao;

    public RespostaAnaliseFinanceira(String documento, String nome, String idProposta, StatusAnalise resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }
}
