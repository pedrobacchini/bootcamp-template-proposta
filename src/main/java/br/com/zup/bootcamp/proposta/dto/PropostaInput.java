package br.com.zup.bootcamp.proposta.dto;

import br.com.zup.bootcamp.proposta.constraint.CPFouCNPJ;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropostaInput {

    @CPFouCNPJ
    @NotBlank
    String documento;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String nome;

    @NotBlank
    String endereco;

    @NotNull
    @Positive
    BigDecimal salario;
}
