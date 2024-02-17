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

    @GetMapping
    public List<DividaTecnicaDTO> obterDividasTecnicasDoUsuario(HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            List<DividaTecnica> dividasTecnicas = dividaTecnicaRepository.findByIdUser(idUser);
            return dividasTecnicas.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter dívidas técnicas do usuário", e);
        }
    }

    @PostMapping
    public ResponseEntity<DividaTecnicaDTO> salvar(@RequestBody DividaTecnicaDTO dividaTecnicaDTO, HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            DividaTecnica dividaTecnica = converterParaEntity(dividaTecnicaDTO);
            dividaTecnica.setIdUser(idUser);
            DividaTecnica savedDividaTecnica = dividaTecnicaRepository.save(dividaTecnica);
            DividaTecnicaDTO savedDividaTecnicaDTO = converterParaDTO(savedDividaTecnica);
            return ResponseEntity.status(HttpStatus.OK).body(savedDividaTecnicaDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a dívida técnica", e);
        }
    }

    @GetMapping("/{id}")
    public DividaTecnicaDTO obterDividaTecnicaPorId(@PathVariable UUID id, HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            DividaTecnica dividaTecnica = dividaTecnicaRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida técnica não encontrada!"));
            if (!dividaTecnica.getIdUser().equals(idUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dívida técnica não pertence ao usuário");
            }
            return converterParaDTO(dividaTecnica);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter dívida técnica por ID", e);
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
    public void deletarDividaTecnica(@PathVariable UUID id, HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            DividaTecnica dividaTecnica = dividaTecnicaRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida técnica não encontrada!"));
            if (!dividaTecnica.getIdUser().equals(idUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dívida técnica não pertence ao usuário");
            }
            dividaTecnicaRepository.delete(dividaTecnica);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar dívida técnica", e);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarDividaTecnica(@PathVariable UUID id, @RequestBody DividaTecnicaDTO dividaTecnicaDTO, HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            DividaTecnica dividaTecnicaAtualizada = converterParaEntity(dividaTecnicaDTO);
            dividaTecnicaAtualizada.setId(id);
            dividaTecnicaAtualizada.setIdUser(idUser);

            DividaTecnica dividaTecnicaExistente = dividaTecnicaRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida técnica não encontrada!"));
            if (!dividaTecnicaExistente.getIdUser().equals(idUser)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dívida técnica não pertence ao usuário");
            }

            dividaTecnicaRepository.save(dividaTecnicaAtualizada);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar dívida técnica", e);
        }
    }

    private DividaTecnicaDTO converterParaDTO(DividaTecnica dividaTecnica) {
        ProjetoDTO projetoDTO = new ProjetoDTO();
        if (dividaTecnica.getProjeto() != null) {
            projetoDTO.setId(dividaTecnica.getProjeto().getId());
            projetoDTO.setNomeDoProjeto(dividaTecnica.getProjeto().getNomeDoProjeto());
        }

        return DividaTecnicaDTO.builder()
                .id(dividaTecnica.getId())
                .nomeDaDividaTecnica(dividaTecnica.getNomeDaDividaTecnica())
                .descricaoDaDT(dividaTecnica.getDescricaoDaDT())
                .projeto(projetoDTO)
                .statusDoPagamento(dividaTecnica.getStatusDoPagamento())
                .statusDaFaseDeGerenciamento(dividaTecnica.getStatusDaFaseDeGerenciamento())
                .diaDoCadastro(dividaTecnica.getDiaDoCadastro())
                .idUser(dividaTecnica.getIdUser())
                .build();
    }

    private DividaTecnica converterParaEntity(DividaTecnicaDTO dividaTecnicaDTO) {
        DividaTecnica dividaTecnica = DividaTecnica.builder()
                .id(dividaTecnicaDTO.getId())
                .nomeDaDividaTecnica(dividaTecnicaDTO.getNomeDaDividaTecnica())
                .descricaoDaDT(dividaTecnicaDTO.getDescricaoDaDT())
                .statusDoPagamento(dividaTecnicaDTO.getStatusDoPagamento())
                .statusDaFaseDeGerenciamento(dividaTecnicaDTO.getStatusDaFaseDeGerenciamento())
                .idUser(dividaTecnicaDTO.getIdUser())
                .build();

        if (dividaTecnicaDTO.getProjetoId() != null) {
            Projeto projeto = projetoRepository.findById(dividaTecnicaDTO.getProjetoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado!"));
            dividaTecnica.setProjeto(projeto);
        }

        return dividaTecnica;
    }
}
