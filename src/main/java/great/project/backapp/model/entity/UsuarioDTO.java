package great.project.backapp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String username;
    private String email;
    private String nomeDaEmpresa;
    private String setorDaEmpresa;
}

