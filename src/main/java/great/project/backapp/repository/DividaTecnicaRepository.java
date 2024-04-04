package great.project.backapp.repository;

import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DividaTecnicaRepository extends JpaRepository<DividaTecnica, UUID> {

    List<DividaTecnica> findByIdUser(UUID idUser);

    Long countByIdUser(UUID idUser);

    List<DividaTecnica> findByProjetoId(UUID id);


    Long countByTipoDeDividaTecnicaAndIdUser(TipoDeDividaTecnica tipo, UUID idUser);

    Long countByStatusDoPagamentoDTAndIdUser(StatusDoPagamentoDT status, UUID idUser);

    List<DividaTecnica> findByProjeto(Projeto projeto);

}
