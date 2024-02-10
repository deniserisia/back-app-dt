package great.project.backapp.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity(name ="Projetos")
public class Projeto {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String nomeDoProjeto;
    private String descricao;
    private String setor;
    private String empresa;
    private String nomeDoLiderDoProjeto;
    private String statusProjeto;

    //@Enumerated(EnumType.STRING)  // Mapeia o enum como String
    //@Column(name = "statusProjeto")  // Renomeando para evitar conflito com a palavra-chave 'status'
    //private StatusProjeto statusProjeto;

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
