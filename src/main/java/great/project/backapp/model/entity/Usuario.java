package great.project.backapp.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name ="Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true, name = "login")
    private String username;

    @Column(name = "senha")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "nomeDaEmpresa")
    private String nomeDaEmpresa;

    @Column(name = "setorDaEmpresa")
    private String setorDaEmpresa;

}
