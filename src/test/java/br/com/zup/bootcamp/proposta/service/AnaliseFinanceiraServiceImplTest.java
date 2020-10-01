package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import br.com.zup.bootcamp.proposta.integration.AnaliseFinanceiraProxy;
import br.com.zup.bootcamp.proposta.integration.RespostaAnaliseFinanceira;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static br.com.zup.bootcamp.proposta.util.TestUtils.propostaValida;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnaliseFinanceiraServiceImplTest {

    AnaliseFinanceiraProxy analiseFinanceiraProxy = mock(AnaliseFinanceiraProxy.class);
    AnaliseFinanceiraService analiseFinanceiraService = new AnaliseFinanceiraServiceImpl(analiseFinanceiraProxy);

    @Test
    @DisplayName("Deve implementar AnaliseFinanceiraService")
    void MUST_ImplementInterface() {
        assertThat(analiseFinanceiraService).isInstanceOf(AnaliseFinanceiraService.class);
    }

    @ParameterizedTest
    @EnumSource(StatusAnalise.class)
    @DisplayName("Dado uma proposta resposta de analise financeira a analise deve retornar o seu status")
    void GIVEN_Proposta_MUST_Call_analisar(StatusAnalise status) {
        Proposta proposta = propostaValida();
        RespostaAnaliseFinanceira expected = new RespostaAnaliseFinanceira(
                proposta.getDocumento(), proposta.getNome(), proposta.getId().toString(), status
        );
        when(analiseFinanceiraProxy.analisar(any())).thenReturn(expected);

        StatusAnalise actual = analiseFinanceiraService.analisar(proposta);

        verify(analiseFinanceiraProxy, times(1)).analisar(any());
        assertThat(actual).isEqualTo(expected.getResultadoSolicitacao());
    }

    @Test
    @DisplayName("Testando FeignException")
    void WHEN_Proxy_Throw_FeignException_MUST_Throw_FeignException() {
        Proposta proposta = propostaValida();
        when(analiseFinanceiraProxy.analisar(any())).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> analiseFinanceiraService.analisar(proposta));
    }
}
