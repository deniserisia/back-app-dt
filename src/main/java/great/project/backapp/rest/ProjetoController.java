package great.project.backapp.rest;

import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.Projeto;
import great.project.backapp.repository.ProjetoRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gerente/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

   // @GetMapping("/todos")
   // public List<Projeto> obterTodos(){
    //    return projetoRepository.findAll();
    //}
    //@GetMapping("/obterProjetosDoUsuario")
    //public List<Projeto> obterProjetosDoUsuario(HttpServletRequest request) {
      //  try {
        //    var idUser = request.getAttribute("idUser");
          //  var projeto = projetoRepository.findByIdUser((UUID) idUser);
          //  return projeto;
       // } catch (Exception e) {
        //    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter projetos do usuário", e);
       // }
    //}

    @GetMapping("/todos")
    public List<Projeto> obterTodos(HttpServletRequest request){
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            return projetoRepository.findByIdUser(idUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter todos os projetos do usuário", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> obterProjetoPorId(@PathVariable UUID id) {
        try {
            Optional<Projeto> projetoOptional = projetoRepository.findById(id);
            return projetoOptional.map(projeto -> ResponseEntity.ok().body(projeto))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> obterProjetos() {
        List<Projeto> projetos = projetoRepository.findAll();
        List<ProjetoDTO> projetoDTOs = projetos.stream()
                .map(projeto -> {
                    ProjetoDTO projetoDTO = new ProjetoDTO();
                    projetoDTO.setId(projeto.getId());
                    projetoDTO.setNomeDoProjeto(projeto.getNomeDoProjeto());
                    return projetoDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(projetoDTOs);
    }


    @GetMapping("/pesquisar")
    public List<Projeto> pesquisarProjetosBD(
            @RequestParam(value = "nomeDoProjeto", required = false, defaultValue = "") String nomeDoProjeto,
            @RequestParam(value = "empresa", required = false, defaultValue = "") String empresa
    ) {
        return projetoRepository.findByNomeDoProjetoAndEmpresa("%" + nomeDoProjeto + "%", empresa);
    }


    @PostMapping
    public ResponseEntity salvar(@RequestBody Projeto projeto, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        projeto.setIdUser((UUID) idUser);
        var project = this.projetoRepository.save(projeto);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }


    @GetMapping("/count")
    public ResponseEntity<Long> obterNumeroDeProjetosDoUsuario(HttpServletRequest request) {
        try {
            var idUser = request.getAttribute("idUser");
            Long numeroDeProjetos = projetoRepository.countByIdUser((UUID) idUser);
            return ResponseEntity.ok(numeroDeProjetos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProjeto( @PathVariable UUID id ){
        projetoRepository
                .findById(id)
                .map( projeto -> {
                    projetoRepository.delete(projeto);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado!") );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarProjeto( @PathVariable UUID id, @RequestBody Projeto projetoAtualizado, HttpServletRequest request ) {
        projetoRepository
                .findById(id)
                .map( projeto -> {
                    projeto.setNomeDoProjeto(projetoAtualizado.getNomeDoProjeto());
                    projeto.setNomeDoLiderDoProjeto(projetoAtualizado.getNomeDoLiderDoProjeto());
                    projeto.setEmpresa(projetoAtualizado.getEmpresa());
                    projeto.setDescricao(projetoAtualizado.getDescricao());
                    projeto.setSetor(projetoAtualizado.getSetor());

                    return projetoRepository.save(projeto);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado!") );
    }
}
