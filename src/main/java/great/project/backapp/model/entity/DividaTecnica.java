package great.project.backapp.model.entity;

import great.project.backapp.model.StatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name ="Dividas_Tecnicas")
public class DividaTecnica {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "tipo_de_divida_tecnica")
    @Enumerated(EnumType.STRING)
    private TipoDeDividaTecnica tipoDeDividaTecnica;

    private String descricaoDaDT;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @Column(name = "status_pagamento")
    @Enumerated(EnumType.STRING)
    private StatusDoPagamentoDT statusDoPagamentoDT;

    @Column(name = "fase_gerenciamento")
    @Enumerated(EnumType.STRING)
    private StatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamentoDT;

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;

    private UUID idUser;

}
