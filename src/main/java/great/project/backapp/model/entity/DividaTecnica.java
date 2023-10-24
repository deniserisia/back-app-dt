package great.project.backapp.model.entity;

import great.project.backapp.model.SatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
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

    @Column(unique = true)
    private String nomeDaDividaTecnica;
    private String descricaoDaDT;


    @JoinColumn(name = "statusDoPagamento")
    private StatusDoPagamentoDT statusDoPagamento;

    @JoinColumn(name = "statusDaFaseDeGerenciamento")
    private SatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamento;

    @CreationTimestamp
    private LocalDateTime diaDoCadastro;

    // Relação muitos para um com Projeto
    @ManyToOne
    @JoinColumn(name = "projeto_id") // Coluna que representa a chave estrangeira para o projeto
    private Projeto projeto;
}
