package br.com.zup.bootcamp.proposta.integration;

import br.com.zup.bootcamp.proposta.helper.IntegrationHelper;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import br.com.zup.bootcamp.proposta.util.GenerateCpfCnpj;
import br.com.zup.bootcamp.proposta.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static br.com.zup.bootcamp.proposta.util.IntegrationUtils.buildArguments;
import static br.com.zup.bootcamp.proposta.util.IntegrationUtils.getIdFromLocation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PropostaIT extends IntegrationHelper {

    @Autowired
    private PropostaRepository repository;

    @Test
    @DisplayName("Dado um payload valido uma proposta deve ser criada")
    void GIVEN_ValidPayload_MUST_ReturnCreated() throws Exception {

        // given
        var expected = propostaValida();

        // when
        var criarProposta = mockMvc.perform(post("/propostas")
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(expected)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // then
        var id = getIdFromLocation(criarProposta.getResponse());
        var propostaOptional = repository.findById(id);
        assertThat(propostaOptional).isNotEmpty();
        var actual = propostaOptional.get();
        assertThat(actual.getDocumento()).isEqualTo(expected.get("documento"));
        assertThat(actual.getEmail()).isEqualTo(expected.get("email"));
        assertThat(actual.getNome()).isEqualTo(expected.get("nome"));
        assertThat(actual.getEndereco()).isEqualTo(expected.get("endereco"));
        assertThat(actual.getSalario()).isEqualTo(expected.get("salario"));
    }

    @ParameterizedTest
    @MethodSource("provedorDadosInvalidos")
    @DisplayName("Dado um payload invalido deve retornar BAD_REQUEST informando os campos invalidos")
    void GIVEN_InvalidPayload_MUST_ReturnBadRequest(String documento,
                                                    String email,
                                                    String nome,
                                                    String endereco,
                                                    BigDecimal salario,
                                                    String[] errorsFields,
                                                    String[] errorsDetails) throws Exception {
        // given
        Map<String, Object> payload = new HashMap<>();
        payload.put("documento", documento);
        payload.put("email", email);
        payload.put("nome", nome);
        payload.put("endereco", endereco);
        payload.put("salario", salario);

        // when
        mockMvc.perform(post("/propostas")
                .contentType("application/json")
                .content(JsonUtil.toJson(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", is("Erro de validação")))
                .andExpect(jsonPath("$.erros[*].campo", containsInAnyOrder(errorsFields)))
                .andExpect(jsonPath("$.erros[*].erro", containsInAnyOrder(errorsDetails)));
    }

    private static Stream<Arguments> provedorDadosInvalidos() {
        var propostaValida = propostaValida();
        List<Arguments> arguments = new ArrayList<>();
        arguments.add(buildArguments(propostaValida, 0, null, new String[]{"documento"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 0, "", new String[]{"documento", "documento"}, new String[]{"não deve estar em branco", "CPF/CNPJ inválido!"}));
        arguments.add(buildArguments(propostaValida, 0, "02717049181", new String[]{"documento"}, new String[]{"CPF/CNPJ inválido!"}));
        arguments.add(buildArguments(propostaValida, 1, null, new String[]{"email"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 1, "", new String[]{"email"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 1, "  ", new String[]{"email", "email"}, new String[]{"não deve estar em branco", "deve ser um endereço de e-mail bem formado"}));
        arguments.add(buildArguments(propostaValida, 1, "email@com.", new String[]{"email"}, new String[]{"deve ser um endereço de e-mail bem formado"}));
        arguments.add(buildArguments(propostaValida, 1, "emailcom.", new String[]{"email"}, new String[]{"deve ser um endereço de e-mail bem formado"}));
        arguments.add(buildArguments(propostaValida, 2, null, new String[]{"nome"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 2, "", new String[]{"nome"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 2, "  ", new String[]{"nome"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 3, null, new String[]{"endereco"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 3, "", new String[]{"endereco"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 3, " ", new String[]{"endereco"}, new String[]{"não deve estar em branco"}));
        arguments.add(buildArguments(propostaValida, 4, null, new String[]{"salario"}, new String[]{"não deve ser nulo"}));
        arguments.add(buildArguments(propostaValida, 4, BigDecimal.ZERO, new String[]{"salario"}, new String[]{"deve ser maior que 0"}));
        return arguments.stream();
    }

    private static LinkedHashMap<String, Object> propostaValida() {
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));

        var payload = new LinkedHashMap<String, Object>();
        payload.put("documento", documento);
        payload.put("email", email);
        payload.put("nome", nome);
        payload.put("endereco", endereco);
        payload.put("salario", salario);

        return payload;
    }
}
