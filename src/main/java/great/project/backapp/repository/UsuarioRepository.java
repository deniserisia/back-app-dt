package great.project.backapp.repository;

import great.project.backapp.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Usuario findByUsername(String username);

    boolean existsByUsername(String username);
}
