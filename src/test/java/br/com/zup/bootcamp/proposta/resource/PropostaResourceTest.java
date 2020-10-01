package br.com.zup.bootcamp.proposta.resource;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.helper.TestHelper;
import br.com.zup.bootcamp.proposta.service.PropostaServiceImpl;
import br.com.zup.bootcamp.proposta.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

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
        var expect = TestUtils.propostaValida();
        var propostaInput = new PropostaInput(
                expect.getDocumento(),
                expect.getEmail(),
                expect.getNome(),
                expect.getEndereco(),
                expect.getSalario()
        );

        var propostaService = mock(PropostaServiceImpl.class);
        when(propostaService.criar(propostaInput)).thenReturn(expect);

        // when
        PropostaResource propostaResource = new PropostaResource(propostaService);
        ResponseEntity<Void> responseEntity = propostaResource.criar(propostaInput);

        // then
        verify(propostaService, times(1)).criar(propostaInput);
        assertThat(responseEntity.getHeaders().getLocation())
                .isEqualTo(new URI(String.format("%s/%s", ENDPOINT_PATH, expect.getId())));
    }
}
