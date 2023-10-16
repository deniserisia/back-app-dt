package great.project.backapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;
}
