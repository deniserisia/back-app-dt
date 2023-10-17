package great.project.backapp.repository;

import great.project.backapp.model.entity.DividaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DividaTecnicaRepository extends JpaRepository<DividaTecnica, UUID> {
}
