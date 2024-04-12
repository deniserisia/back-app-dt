package great.project.backapp.repository;

import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DividaTecnicaRepository extends JpaRepository<DividaTecnica, Long> {

    List<DividaTecnica> findByIdUser(Long idUser);


    //@Query("select  )
    Long countByIdUser(Long idUser);

    @Query("select d from DividaTecnica d where d.projeto.id=:id")
    List<DividaTecnica> findByProjetoId(@Param("id") Long id);


    Long countByTipoDeDividaTecnicaAndIdUser(TipoDeDividaTecnica tipo, Long idUser);

    Long countByStatusDoPagamentoDTAndIdUser(StatusDoPagamentoDT status, Long idUser);

    List<DividaTecnica> findByProjeto(Projeto projeto);

}
