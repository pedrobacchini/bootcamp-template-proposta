package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.dto.AnaliseRequest;
import br.com.zup.bootcamp.proposta.dto.AnaliseResponse;
import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.exception.PropostaDuplicadaException;
import br.com.zup.bootcamp.proposta.mapper.PropostaMapper;
import br.com.zup.bootcamp.proposta.proxy.AnaliseProxy;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropostaServiceImpl implements PropostaService {

    private final PropostaMapper propostaMapper;
    private final PropostaRepository propostaRepository;
    private final AnaliseProxy analiseProxy;

    @Override
    public Proposta criar(PropostaInput propostaInput) {
        if (propostaRepository.findByDocumento(propostaInput.getDocumento()).isPresent()) {
            throw new PropostaDuplicadaException();
        } else {
            Proposta proposta = propostaMapper.toEntity(propostaInput);
            proposta = propostaRepository.save(proposta);
            AnaliseResponse response = analiseProxy.analisar(new AnaliseRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId().toString()));
            proposta.atualizarStatus(response.getResultadoSolicitacao());
            return propostaRepository.save(proposta);
        }
    }
}
