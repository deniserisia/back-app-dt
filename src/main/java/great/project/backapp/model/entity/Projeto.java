package great.project.backapp.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.StatusProjeto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name ="Projeto")

public class Projeto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }

    // Relação um para muitos com DividaTecnica

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DividaTecnica> dividasTecnicas= new ArrayList<>();


    public Projeto () {
    }

}
