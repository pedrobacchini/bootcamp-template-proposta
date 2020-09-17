package br.com.zup.bootcamp.proposta.dto;

import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnaliseResponse {

    String documento;

    String nome;

    String idProposta;

    StatusAnalise resultadoSolicitacao;

    public AnaliseResponse(String documento, String nome, String idProposta, StatusAnalise resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }
}
