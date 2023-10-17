package great.project.backapp.model.entity;

import jakarta.persistence.*;
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
@Entity(name ="Dividas_Tecnicas")
public class DividaTecnica {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String NomeDoProjeto;
    private String descricaoDaDT;

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;

    // Relação muitos para um com Projeto
    @ManyToOne
    @JoinColumn(name = "projeto_id") // Coluna que representa a chave estrangeira
    private Projeto projeto;
}
