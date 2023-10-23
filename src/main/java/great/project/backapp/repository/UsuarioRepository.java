package great.project.backapp.repository;

import great.project.backapp.model.UsuarioRoles;
import great.project.backapp.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Usuario findByUsername(String username);

    boolean existsByUsername(String username);

    //List<Usuario> findByRolesContaining(UsuarioRoles role);
}
