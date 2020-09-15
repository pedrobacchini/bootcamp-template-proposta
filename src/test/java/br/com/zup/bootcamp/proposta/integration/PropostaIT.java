package br.com.zup.bootcamp.proposta.integration;

import br.com.zup.bootcamp.proposta.helper.IntegrationHelper;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import br.com.zup.bootcamp.proposta.util.GenerateCpfCnpj;
import br.com.zup.bootcamp.proposta.util.JsonUtil;
import br.com.zup.bootcamp.proposta.util.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PropostaIT extends IntegrationHelper {

    @Autowired
    private PropostaRepository repository;

    @Test
    void GIVEN_ValidPayload_MUST_ReturnCreated() throws Exception {

        // given
        var expected = propostaValida();

        // when
        var createCompany = mockMvc.perform(post("/propostas")
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.toJson(expected)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // then
        var id = MockUtils.getIdFromLocation(createCompany.getResponse());
        var companyOptional = repository.findById(id);
        assertThat(companyOptional).isNotEmpty();
        var actual = companyOptional.get();
        assertThat(actual.getDocumento()).isEqualTo(expected.get("documento"));
        assertThat(actual.getEmail()).isEqualTo(expected.get("email"));
        assertThat(actual.getNome()).isEqualTo(expected.get("nome"));
        assertThat(actual.getNome()).isEqualTo(expected.get("nome"));
        assertThat(actual.getEndereco()).isEqualTo(expected.get("endereco"));
        assertThat(actual.getSalario()).isEqualTo(expected.get("salario"));
    }

    private static Map<String, Object> propostaValida() {
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));

        var payload = new HashMap<String, Object>();
        payload.put("documento", documento);
        payload.put("email", email);
        payload.put("nome", nome);
        payload.put("endereco", endereco);
        payload.put("salario", salario);

        return payload;
    }
}
