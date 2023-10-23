package great.project.backapp.model.entity;


import great.project.backapp.model.UsuarioRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(unique = true)
    private String username;
    private String nome;
    private String password;

    @ElementCollection(targetClass = UsuarioRoles.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "UsuarioRoles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    private Set<UsuarioRoles> roles;


    @CreationTimestamp
    private LocalDateTime dataDaCriacaoDoUsuario;
}
