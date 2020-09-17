package br.com.zup.bootcamp.proposta.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnaliseRequest {

    String documento;

    String nome;

    String idProposta;

    public AnaliseRequest(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }
}
