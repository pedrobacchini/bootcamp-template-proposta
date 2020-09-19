package br.com.zup.bootcamp.proposta.integration;

import br.com.zup.bootcamp.proposta.enumerated.StatusProposta;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static br.com.zup.bootcamp.proposta.util.IntegrationUtils.buildArguments;
import static br.com.zup.bootcamp.proposta.util.IntegrationUtils.getIdFromLocation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static br.com.zup.bootcamp.proposta.resource.PropostaResource.ENDPOINT_PATH;
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
        var criarProposta = mockMvc.perform(post(ENDPOINT_PATH)
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
        assertThat(actual.getStatus()).isEqualTo(StatusProposta.ELEGIVEL);
        assertThat(actual.getAuditoria().getDataCriacao()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(actual.getAuditoria().getDataUltimaModificacao()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @ParameterizedTest
    @MethodSource("provedorDadosInvalidos")
    @DisplayName("Dado um conjunto de payload's invalido deve retornar BAD_REQUEST informando os campos invalidos")
    void GIVEN_InvalidPayload_MUST_ReturnBadRequest(Map<String, Object> payload,
                                                    String[] errorsFields,
                                                    String[] errorsDetails) throws Exception {
        mockMvc.perform(post(ENDPOINT_PATH)
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", is("Erro de validação")))
                .andExpect(jsonPath("$.erros[*].campo", containsInAnyOrder(errorsFields)))
                .andExpect(jsonPath("$.erros[*].erro", containsInAnyOrder(errorsDetails)));
    }

    @Test
    @DisplayName("Dado um payload valido uma proposta duplicada deve retornar UNPROCESSABLE_ENTITY")
    void GIVEN_ValidPayload_AND_Duplicated_MUST_ReturnUnprocessableEntity() throws Exception {
        // given
        var expected = propostaValida();
        expected.put("documento", "45518154000108");

        // when
        mockMvc.perform(post(ENDPOINT_PATH)
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", is("Proposta já cadastrada")));
    }

    private static Stream<Arguments> provedorDadosInvalidos() {
        var propostaValida = propostaValida();
        List<Arguments> arguments = new ArrayList<>();
        arguments.add(buildArguments(propostaValida, "documento", null, "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "documento", "", "não deve estar em branco", "CPF/CNPJ inválido!"));
        arguments.add(buildArguments(propostaValida, "documento", "02717049181", "CPF/CNPJ inválido!"));
        arguments.add(buildArguments(propostaValida, "email", null, "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "email", "", "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "email", "  ", "não deve estar em branco", "deve ser um endereço de e-mail bem formado"));
        arguments.add(buildArguments(propostaValida, "email", "email@com.", "deve ser um endereço de e-mail bem formado"));
        arguments.add(buildArguments(propostaValida, "email", "emailcom.", "deve ser um endereço de e-mail bem formado"));
        arguments.add(buildArguments(propostaValida, "nome", null, "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "nome", "", "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "nome", "  ", "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "endereco", null, "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "endereco", "", "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "endereco", " ", "não deve estar em branco"));
        arguments.add(buildArguments(propostaValida, "salario", null, "não deve ser nulo"));
        arguments.add(buildArguments(propostaValida, "salario", BigDecimal.ZERO, "deve ser maior que 0"));
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
