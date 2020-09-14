package br.com.zup.bootcamp.proposta.entity;

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
    private String documento;

    @Setter
    private String email;

    @Setter
    private String nome;

    @Setter
    private String endereco;

    @Setter
    private BigDecimal salario;
}
