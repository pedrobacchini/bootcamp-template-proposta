package br.com.zup.bootcamp.proposta.mapper;

import br.com.zup.bootcamp.proposta.dto.PropostaInput;
import br.com.zup.bootcamp.proposta.entity.Proposta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropostaMapper {

    Proposta toEntity(PropostaInput propostaInput);
}
