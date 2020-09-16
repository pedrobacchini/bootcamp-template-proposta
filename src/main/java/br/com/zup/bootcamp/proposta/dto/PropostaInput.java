package br.com.zup.bootcamp.proposta.dto;

import br.com.zup.bootcamp.proposta.constraint.CPFouCNPJ;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    public PropostaInput(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
                         @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }
}
