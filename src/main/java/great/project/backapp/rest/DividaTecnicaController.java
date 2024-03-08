package great.project.backapp.rest;

import great.project.backapp.model.TipoDeDividaTecnica;
import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;

import great.project.backapp.repository.DividaTecnicaRepository;
import great.project.backapp.repository.ProjetoRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/contagem-dividas-tecnicas-por-mes-no-ano")
    public ResponseEntity<Map<String, Long>> obterContagemDividasTecnicasPorMesNoAno(
            @RequestParam("ano") int ano,
            HttpServletRequest request
    ) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            List<DividaTecnica> dividasTecnicas = dividaTecnicaRepository.findByIdUser(idUser);

            // Filtrar dívidas técnicas para o ano especificado
            dividasTecnicas = dividasTecnicas.stream()
                    .filter(dividaTecnica -> dividaTecnica.getDiaDoCadastro().getYear() == ano)
                    .collect(Collectors.toList());

            // Criar uma lista com os nomes dos meses
            String[] nomesDosMeses = {
                    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            };

            // Agrupar dívidas técnicas por mês
            Map<Month, Long> contagemPorMes = new HashMap<>();
            for (Month month : Month.values()) {
                long count = dividasTecnicas.stream()
                        .filter(dividaTecnica -> dividaTecnica.getDiaDoCadastro().getMonth() == month)
                        .count();
                contagemPorMes.put(month, count);
            }

            // Convertendo para o formato de resposta desejado
            Map<String, Long> resultado = contagemPorMes.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> nomesDosMeses[entry.getKey().getValue() - 1], // -1 pois Month enum começa de 1
                            Map.Entry::getValue
                    ));

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/contagem-por-tipo")
    public ResponseEntity<Map<String, Long>> obterContagemDividasPorTipo(HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            Map<String, Long> contagemPorTipo = new HashMap<>();

            for (TipoDeDividaTecnica tipo : TipoDeDividaTecnica.values()) {
                Long contagem = dividaTecnicaRepository.countByTipoDeDividaTecnicaAndIdUser(tipo, idUser);
                contagemPorTipo.put(tipo.name(), contagem);
            }

            return ResponseEntity.ok(contagemPorTipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  atualizarDividaTecnica( @PathVariable UUID id, @RequestBody DividaTecnica dividaTecnicaAtualizada, HttpServletRequest request ) {
        dividaTecnicaRepository
                .findById(id)
                .map( dividaTecnica -> {
                    dividaTecnica.setTipoDeDividaTecnica(dividaTecnicaAtualizada.getTipoDeDividaTecnica());
                    dividaTecnica.setDescricaoDaDT(dividaTecnicaAtualizada.getDescricaoDaDT());
                    dividaTecnica.setProjeto(dividaTecnicaAtualizada.getProjeto());
                    dividaTecnica.setStatusDoPagamentoDT(dividaTecnicaAtualizada.getStatusDoPagamentoDT());
                    dividaTecnica.setStatusDaFaseDeGerenciamentoDT(dividaTecnicaAtualizada.getStatusDaFaseDeGerenciamentoDT());
                    return dividaTecnicaRepository.save(dividaTecnica);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida Técnica não encontrada!") );
    }
}
