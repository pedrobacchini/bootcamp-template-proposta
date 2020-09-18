package br.com.zup.bootcamp.proposta.dto;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnaliseRequest {

    String documento;

    String nome;

    String idProposta;

    public static AnaliseRequest fromProposta(Proposta proposta) {
        AnaliseRequest analiseRequest = new AnaliseRequest();
        analiseRequest.documento = proposta.getDocumento();
        analiseRequest.nome = proposta.getNome();
        analiseRequest.idProposta = proposta.getId().toString();
        return analiseRequest;
    }
}
