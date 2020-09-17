package br.com.zup.bootcamp.proposta.entity;

import br.com.zup.bootcamp.proposta.audit.Auditoria;
import br.com.zup.bootcamp.proposta.enumerated.StatusAnalise;
import br.com.zup.bootcamp.proposta.enumerated.StatusProposta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "propostas")
@EntityListeners(AuditingEntityListener.class)
public class Proposta implements Serializable {

    private static final long serialVersionUID = -9211904481417441803L;

    @Id
    @SequenceGenerator(name = "propostas_id_seq", sequenceName = "propostas_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propostas_id_seq")
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String documento;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String nome;

    @Setter
    @Column(nullable = false)
    private String endereco;

    @Setter
    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(value = EnumType.STRING)
    private StatusProposta status;

    @Embedded
    private final Auditoria auditoria = new Auditoria();

    public Proposta(Long id, String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.id = id;
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public void atualizarStatus(StatusAnalise resultadoSolicitacao) {
        this.status = resultadoSolicitacao == StatusAnalise.SEM_RESTRICAO ? StatusProposta.ELEGIVEL : StatusProposta.NAO_ELEGIVEL;
    }
}
