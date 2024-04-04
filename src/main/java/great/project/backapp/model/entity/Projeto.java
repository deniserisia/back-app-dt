package great.project.backapp.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.StatusProjeto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name ="Projeto")
public class Projeto {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true, name = "nomeDoProjeto")
    private String nomeDoProjeto;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "setor")
    private String setor;
    @Column(name = "empresa")
    private String empresa;
    @Column(name = "nomeDoLiderDoProjeto")
    private String nomeDoLiderDoProjeto;

    @Column(name = "quantidadeDePessoasNoTimeDeDev")
    private Integer quantidadeDePessoasNoTimeDeDev;

    @JoinColumn(name = "statusDoProjeto")
    private StatusProjeto statusProjeto;


    private UUID idUser;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }

    // Relação um para muitos com DividaTecnica
    //@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    //private List<DividaTecnica> dividasTecnicas;
}
