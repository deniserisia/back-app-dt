package great.project.backapp.repository;

import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DividaTecnicaRepository extends JpaRepository<DividaTecnica, UUID> {

    List<DividaTecnica> findByIdUser(UUID idUser);

    Long countByIdUser(UUID idUser);
}
