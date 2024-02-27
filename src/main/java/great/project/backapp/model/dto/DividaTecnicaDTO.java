package great.project.backapp.rest.dto;

import great.project.backapp.model.SatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.dto.ProjetoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividaTecnicaDTO {

    private UUID id;
    private String nomeDaDividaTecnica;
    private String descricaoDaDT;
    private UUID projetoId;
    private StatusDoPagamentoDT statusDoPagamento;
    private SatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamento;
    private LocalDateTime diaDoCadastro;
    private UUID idUser;

    private ProjetoDTO projeto;

    @Builder
    public DividaTecnicaDTO(
            // Outras propriedades
            ProjetoDTO projeto
    ) {
        // Inicialização de outras propriedades
        this.projeto = projeto;
    }
}
