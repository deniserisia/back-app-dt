package great.project.backapp.model.dto;

import lombok.Data;

@Data
public class ContagemPorMesDTO {
    private String mes;
    private Long quantidade;

    public ContagemPorMesDTO(String mes, Long quantidade) {
        this.mes = mes;
        this.quantidade = quantidade;
    }
}
