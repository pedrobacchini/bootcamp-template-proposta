package br.com.zup.bootcamp.proposta.service;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import br.com.zup.bootcamp.proposta.mapper.PropostaMapper;
import br.com.zup.bootcamp.proposta.repository.PropostaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropostaServiceImpl implements PropostaService {

    private final PropostaMapper propostaMapper;
    private final PropostaRepository propostaRepository;

    @Override
    public Proposta criar(PropostaInput propostaInput) {
        Proposta proposta = propostaMapper.toEntity(propostaInput);
        return propostaRepository.save(proposta);
    }
}
