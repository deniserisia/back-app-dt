package great.project.backapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private String NomeDoProjeto;
    private String descricao;
    private String setor;
    private String empresa;
    private String nomeDoLiderDoProjeto;
    private String prioridade;
    private String dataDeInicioDoProjeto;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;

    // Relação um para muitos com DividaTecnica
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private List<DividaTecnica> dividasTecnicas;
}
