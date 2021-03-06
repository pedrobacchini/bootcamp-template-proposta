package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import br.com.zup.bootcamp.proposta.enumerated.StatusProposta;
import br.com.zup.bootcamp.proposta.exception.PropostaDuplicadaException;
import br.com.zup.bootcamp.proposta.helper.TestHelper;
import br.com.zup.bootcamp.proposta.mapper.PropostaMapper;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import br.com.zup.bootcamp.proposta.util.GenerateCpfCnpj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
class PropostaServiceImplTest extends TestHelper {

    PropostaRepository propostaRepository = mock(PropostaRepository.class);
    PropostaMapper propostaMapper = mock(PropostaMapper.class);
    AnaliseFinanceiraService analiseFinanceiraService = mock(AnaliseFinanceiraService.class);
    PropostaService propostaService = new PropostaServiceImpl(propostaMapper, propostaRepository, analiseFinanceiraService);
    final ArgumentCaptor<Proposta> captor = ArgumentCaptor.forClass(Proposta.class);

    @Test
    @DisplayName("Deve implementar PropostaService")
    void MUST_ImplementInterface() {
        assertThat(propostaService).isInstanceOf(PropostaService.class);
    }

    @Test
    @DisplayName("Dado uma proposta valida essa proposta deve ser criada no banco de dados")
    void GIVEN_ValidProposta_MUST_CreateCompanyInDatabase() {
        Long id = 1L;
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));
        var propostaInput = new PropostaInput(documento, email, nome, endereco, salario);
        var expect = new Proposta(id, documento, email, nome, endereco, salario);
        expect.getAuditoria().setDataCriacao(LocalDateTime.now());
        expect.getAuditoria().setDataUltimaModificacao(LocalDateTime.now());
        when(propostaMapper.toEntity(propostaInput)).thenReturn(expect);
        when(propostaRepository.save(expect)).thenReturn(expect);
        when(analiseFinanceiraService.analisar(any())).thenReturn(StatusAnalise.SEM_RESTRICAO);

        propostaService.criar(propostaInput);

        verify(propostaMapper, times(1)).toEntity(propostaInput);
        verify(analiseFinanceiraService, times(1)).analisar(any());
        verify(propostaRepository, times(2)).save(captor.capture());
        Proposta actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getDocumento()).isEqualTo(documento);
        assertThat(actual.getNome()).isEqualTo(nome);
        assertThat(actual.getEndereco()).isEqualTo(endereco);
        assertThat(actual.getSalario()).isEqualTo(salario);
        assertThat(actual.getStatus()).isEqualTo(StatusProposta.ELEGIVEL);
        assertThat(actual.getAuditoria()).isNotNull();
        assertThat(actual.getAuditoria().getDataCriacao()).isEqualTo(expect.getAuditoria().getDataCriacao());
        assertThat(actual.getAuditoria().getDataUltimaModificacao()).isEqualTo(expect.getAuditoria().getDataUltimaModificacao());
    }

    @Test
    @DisplayName("Dado uma proposta valido e duplicada deve retornar PropostaDuplicadaException")
    void GIVEN_ValidPayload_AND_Duplicated_MUST_Throw_PropostaDuplicadaException() {
        var id = 1L;
        var documento = GenerateCpfCnpj.cnpj(false);
        var email = faker.internet().emailAddress();
        var nome = faker.name().fullName();
        var endereco = faker.address().streetAddress();
        var salario = BigDecimal.valueOf(faker.number().randomDouble(2, 1L, 10000L));
        var propostaInput = new PropostaInput(documento, email, nome, endereco, salario);
        var expect = new Proposta(id, documento, email, nome, endereco, salario);

        when(propostaRepository.findByDocumento(documento)).thenReturn(Optional.of(expect));

        Assertions.assertThrows(PropostaDuplicadaException.class, () -> propostaService.criar(propostaInput));
    }
}
