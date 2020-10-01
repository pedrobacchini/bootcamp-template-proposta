package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import br.com.zup.bootcamp.proposta.exception.PropostaDuplicadaException;
import br.com.zup.bootcamp.proposta.mapper.PropostaMapper;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PropostaServiceImpl implements PropostaService {

    private final PropostaMapper propostaMapper;
    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraService analiseFinanceiraService;

    @Override
    public Proposta criar(PropostaInput propostaInput) {
        if (propostaRepository.findByDocumento(propostaInput.getDocumento()).isPresent()) {
            throw new PropostaDuplicadaException();
        } else {
            log.info("Cadastrando nova proposta para {}", propostaInput.getNome());
            Proposta proposta = propostaMapper.toEntity(propostaInput);
            proposta = propostaRepository.save(proposta);
            StatusAnalise statusAnalise = analiseFinanceiraService.analisar(proposta);
            proposta.atualizarStatus(statusAnalise);
            proposta = propostaRepository.save(proposta);
            log.info(
                    "Proposta criada com sucesso! id: {} documento: {} salário: {}",
                    proposta.getId(),
                    proposta.getDocumento(),
                    proposta.getSalario()
            );
            return proposta;
        }
    }
}
