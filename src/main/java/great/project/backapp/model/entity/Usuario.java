package great.project.backapp.model.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Getter
@Setter
@Entity(name ="Usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column (unique = true, name = "login")
    private String username;

    @Column (name = "senha")
    private String password;

    @Column (name = "email")
    private String email;

    @Column (name = "nomeDaEmpresa")
    private String nomeDaEmpresa;

    @Column (name = "setorDaEmpresa")
    private String setorDaEmpresa;

    public Usuario () {
    }
}


