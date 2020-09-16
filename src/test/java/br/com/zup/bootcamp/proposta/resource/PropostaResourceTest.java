package br.com.zup.bootcamp.proposta.resource;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.helper.TestHelper;
import br.com.zup.bootcamp.proposta.service.PropostaServiceImpl;
import br.com.zup.bootcamp.proposta.util.GenerateCpfCnpj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;

import static br.com.zup.bootcamp.proposta.resource.PropostaResource.ENDPOINT_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
class PropostaResourceTest extends TestHelper {

    @Test
    @DisplayName("Dado uma proposta valida o serviço deve ser chamado e um header com a localizacao deve ser retornado")
    void GIVEN_ValidProposta_MUST_CallService() throws Exception {

        // given
        var id = 1L;
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));
        var propostaInput = new PropostaInput(documento, email, nome, endereco, salario);
        Proposta expect = new Proposta(id, documento, email, nome, endereco, salario);

        var propostaService = mock(PropostaServiceImpl.class);
        when(propostaService.criar(propostaInput)).thenReturn(expect);

        // when
        PropostaResource propostaResource = new PropostaResource(propostaService);
        ResponseEntity<Void> responseEntity = propostaResource.criar(propostaInput);

        // then
        verify(propostaService, times(1)).criar(propostaInput);
        assertThat(responseEntity.getHeaders().getLocation()).isEqualTo(new URI(String.format("%s/%s", ENDPOINT_PATH, id)));
    }
}
