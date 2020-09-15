package br.com.zup.bootcamp.proposta.constraint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CPFouCNPJValidatorTest {

    @Test
    @DisplayName("não deve aceitar quando não é cpf ou cnpj")
    void teste1() {
        CPFouCNPJ.Validator validador = new CPFouCNPJ.Validator();
        boolean valido = validador.isValid("", null);
        Assertions.assertFalse(valido);
    }

    @Test
    @DisplayName("deve aceitar quando é cpf")
    void teste2() {
        CPFouCNPJ.Validator validador = new CPFouCNPJ.Validator();
        boolean valido = validador.isValid("82649901004", null);
        Assertions.assertTrue(valido);
    }

    @Test
    @DisplayName("deve aceitar quando é cnpj")
    void teste3() {
        CPFouCNPJ.Validator validador = new CPFouCNPJ.Validator();
        boolean valido = validador.isValid("90778497000166", null);
        Assertions.assertTrue(valido);
    }
}
