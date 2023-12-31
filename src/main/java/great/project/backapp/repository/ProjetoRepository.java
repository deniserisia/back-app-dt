package great.project.backapp.repository;

import great.project.backapp.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {

    List<Projeto> findByIdUser(UUID idUser);
}
