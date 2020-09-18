package br.com.zup.bootcamp.proposta.repository;

import br.com.zup.bootcamp.proposta.entity.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    @Transactional(readOnly = true)
    Optional<Proposta> findByDocumento(String documento);
}
