package great.project.backapp.model.dto;

import great.project.backapp.model.StatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class DividaTecnicaDTO {

    private UUID id;
    private String nomeDaDividaTecnica;
    private String descricaoDaDT;
    private UUID projetoId;
    private StatusDoPagamentoDT statusDoPagamento;
    private StatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamentoDT;
    private LocalDateTime diaDoCadastro;
    private UUID idUser;

    private ProjetoDTO projeto;

    @Builder
    public DividaTecnicaDTO(
            UUID id,
            String nomeDaDividaTecnica,
            String descricaoDaDT,
            UUID projetoId,
            StatusDoPagamentoDT statusDoPagamento,
            StatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamentoDT,
            LocalDateTime diaDoCadastro,
            UUID idUser,
            ProjetoDTO projeto
    ) {
        this.id = id;
        this.nomeDaDividaTecnica = nomeDaDividaTecnica;
        this.descricaoDaDT = descricaoDaDT;
        this.projetoId = projetoId;
        this.statusDoPagamento = statusDoPagamento;
        this.statusDaFaseDeGerenciamentoDT = statusDaFaseDeGerenciamentoDT;
        this.diaDoCadastro = diaDoCadastro;
        this.idUser = idUser;
        this.projeto = projeto;
    }
}
