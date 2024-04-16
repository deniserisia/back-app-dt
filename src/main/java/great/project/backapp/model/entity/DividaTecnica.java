package great.project.backapp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import great.project.backapp.model.StatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode
@Table(name = "dividas_tecnicas")
public class DividaTecnica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(generator = "UUID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tipo_de_divida_tecnica")
    @Enumerated(EnumType.STRING)
    private TipoDeDividaTecnica tipoDeDividaTecnica;

    private String descricaoDaDT;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @Column(name = "esforcoDoPagamento")
    private Double esforcoDoPagamento;

    @Column(name = "causaDaDT")
    private String causaDaDT;

    @Column(name = "resultadoDoesforcoDoPagammento")
    private Double resultadoDoesforcoDoPagammento;

    @Column(name = "quantidadeDePessoas")
    private Integer quantidadeDePessoas;

    @Column(name = "valorPorHoraDeTrabalho")
    private Double valorPorHoraDeTrabalho;

    @Column(name = "status_pagamento")
    @Enumerated(EnumType.STRING)
    private StatusDoPagamentoDT statusDoPagamentoDT;

    @Column(name = "fase_gerenciamento")
    @Enumerated(EnumType.STRING)
    private StatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamentoDT;

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;

    private Long idUser;

    @Transient
    private Long id_projeto;

    public DividaTecnica () {
    }

    public Double getResultadoDoesforcoDoPagammento () {
        return resultadoDoesforcoDoPagammento;
    }

    public void setResultadoDoesforcoDoPagammento (Double resultadoDoesforcoDoPagammento) {
        this.resultadoDoesforcoDoPagammento = resultadoDoesforcoDoPagammento;
    }

    @Override
    public String toString () {
        return "DividaTecnica{" +
                "id=" + id +
                ", tipoDeDividaTecnica=" + tipoDeDividaTecnica +
                ", descricaoDaDT='" + descricaoDaDT + '\'' +
                ", projeto=" + projeto +
                ", esforcoDoPagamento=" + esforcoDoPagamento +
                ", causaDaDT='" + causaDaDT + '\'' +
                ", resultadoDoesforcoDoPagammento=" + resultadoDoesforcoDoPagammento +
                ", quantidadeDePessoas=" + quantidadeDePessoas +
                ", valorPorHoraDeTrabalho=" + valorPorHoraDeTrabalho +
                ", statusDoPagamentoDT=" + statusDoPagamentoDT +
                ", statusDaFaseDeGerenciamentoDT=" + statusDaFaseDeGerenciamentoDT +
                ", diaDoCadastro=" + diaDoCadastro +
                ", idUser=" + idUser +
                ", id_projeto=" + id_projeto +
                '}';
    }
}
