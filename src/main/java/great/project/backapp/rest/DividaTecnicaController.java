package great.project.backapp.rest;

import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import great.project.backapp.rest.dto.DividaTecnicaDTO;
import great.project.backapp.repository.DividaTecnicaRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gerente/divida-tecnica")
public class DividaTecnicaController {

    @Autowired
    private DividaTecnicaRepository dividaTecnicaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;



    @PostMapping
    public ResponseEntity salvar(@RequestBody DividaTecnica dividaTecnica, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        dividaTecnica.setIdUser((UUID) idUser);
        try {
            var dividaTecnicaSalva = this.dividaTecnicaRepository.save(dividaTecnica);
            return ResponseEntity.status(HttpStatus.OK).body(dividaTecnicaSalva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a DividaTecnica.");
        }
    }



    @GetMapping("/count")
    public ResponseEntity<Long> obterNumeroDeDividasTecnicasDoUsuario(HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            Long numeroDeDT = dividaTecnicaRepository.countByIdUser(idUser);
            return ResponseEntity.ok(numeroDeDT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarDT( @PathVariable UUID id ){
        dividaTecnicaRepository
                .findById(id)
                .map( dividaTecnica -> {
                    dividaTecnicaRepository.delete(dividaTecnica);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DT não encontrado!") );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  atualizarDividaTecnica( @PathVariable UUID id, @RequestBody DividaTecnica dividaTecnicaAtualizada, HttpServletRequest request ) {
        dividaTecnicaRepository
                .findById(id)
                .map( dividaTecnica -> {
                    dividaTecnica.setNomeDaDividaTecnica(dividaTecnicaAtualizada.getNomeDaDividaTecnica());
                    dividaTecnica.setDescricaoDaDT(dividaTecnicaAtualizada.getDescricaoDaDT());
                    dividaTecnica.setProjeto(dividaTecnicaAtualizada.getProjeto());
                    dividaTecnica.setStatusDoPagamento(dividaTecnicaAtualizada.getStatusDoPagamento());
                    dividaTecnica.setStatusDaFaseDeGerenciamento(dividaTecnicaAtualizada.getStatusDaFaseDeGerenciamento());
                    return dividaTecnicaRepository.save(dividaTecnica);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida Técnica não encontrada!") );
    }
}
