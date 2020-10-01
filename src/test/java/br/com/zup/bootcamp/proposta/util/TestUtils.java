package br.com.zup.bootcamp.proposta.util;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class TestUtils {

    protected static final Faker faker = new Faker();

    public static LinkedHashMap<String, Object> propostaValidaPayload() {

        Proposta proposta = propostaValida();

        var payload = new LinkedHashMap<String, Object>();
        payload.put("documento", proposta.getDocumento());
        payload.put("email", proposta.getEmail());
        payload.put("nome", proposta.getNome());
        payload.put("endereco", proposta.getEndereco());
        payload.put("salario", proposta.getSalario());

        return payload;
    }

    public static Proposta propostaValida() {
        var id = 1L;
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));

        return new Proposta(id, documento, email, nome, endereco, salario);
    }
}
