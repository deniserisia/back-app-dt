package great.project.backapp.repository;

import great.project.backapp.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {

    List<Projeto> findByIdUser(UUID idUser);

    Long countByIdUser(UUID idUser);

    @Query("select c from Projeto c " +
            "where upper(c.nomeDoProjeto) like upper(:nomeDoProjeto) and upper(c.empresa) like upper(:empresa)")
    List<Projeto> findByNomeDoProjetoAndEmpresa(
            @Param("nomeDoProjeto") String nomeDoProjeto, @Param("empresa") String empresa);
}
