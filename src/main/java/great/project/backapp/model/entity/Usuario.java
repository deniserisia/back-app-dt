package great.project.backapp.model.entity;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime dataDaCriacaoDoUsuario;
}
