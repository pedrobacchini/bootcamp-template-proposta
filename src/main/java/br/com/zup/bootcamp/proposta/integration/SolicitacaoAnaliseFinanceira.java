package br.com.zup.bootcamp.proposta.integration;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolicitacaoAnaliseFinanceira {

    String documento;

    String nome;

    String idProposta;

    public static SolicitacaoAnaliseFinanceira fromProposta(Proposta proposta) {
        SolicitacaoAnaliseFinanceira solicitacaoAnaliseFinanceira = new SolicitacaoAnaliseFinanceira();
        solicitacaoAnaliseFinanceira.documento = proposta.getDocumento();
        solicitacaoAnaliseFinanceira.nome = proposta.getNome();
        solicitacaoAnaliseFinanceira.idProposta = proposta.getId().toString();
        return solicitacaoAnaliseFinanceira;
    }
}
