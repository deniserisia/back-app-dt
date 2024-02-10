package great.project.backapp.rest;

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
import java.util.UUID;

@RestController
@RequestMapping("/gerente")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @GetMapping
    public List<Projeto> obterTodos(){
        return projetoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody Projeto projeto, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        projeto.setIdUser((UUID) idUser);
        var project = this.projetoRepository.save(projeto);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @GetMapping("/{id}")
    public List<Projeto> listarProjetosPorId(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var projetos = this.projetoRepository.findByIdUser((UUID) idUser);
        return projetos;
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
