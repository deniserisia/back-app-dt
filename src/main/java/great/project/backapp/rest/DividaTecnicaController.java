package great.project.backapp.rest;

import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import great.project.backapp.model.dto.DividaTecnicaDTO;
import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;

import great.project.backapp.repository.DividaTecnicaRepository;
import great.project.backapp.repository.ProjetoRepository;
import great.project.backapp.service.DividaTecnicaService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gerente/divida-tecnica")
public class DividaTecnicaController {

    @Autowired
    private DividaTecnicaRepository dividaTecnicaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private DividaTecnicaService dividaTecnicaService;

    public DividaTecnicaController(DividaTecnicaService dividaTecnicaService) {
        this.dividaTecnicaService = dividaTecnicaService;
    }


    @GetMapping("/todas")
    public List<DividaTecnicaDTO> obterTodas(HttpServletRequest request){
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            List<DividaTecnica> dividasTecnicas = dividaTecnicaRepository.findByIdUser(idUser);

            List<DividaTecnicaDTO> dividasTecnicasDTO = new ArrayList<>();
            for (DividaTecnica dividaTecnica : dividasTecnicas) {
                Projeto projeto = dividaTecnica.getProjeto();
                String nomeDoProjeto = projeto != null ? projeto.getNomeDoProjeto() : null;

                dividasTecnicasDTO.add(DividaTecnicaDTO.builder()
                        .id(dividaTecnica.getId())
                        .nomeDaDividaTecnica(dividaTecnica.getTipoDeDividaTecnica().name()) // Se nomeDaDividaTecnica for um campo separado, substitua por ele
                        .descricaoDaDT(dividaTecnica.getDescricaoDaDT())
                        .projetoId(projeto != null ? projeto.getId() : null)
                        .statusDoPagamento(dividaTecnica.getStatusDoPagamentoDT())
                        .statusDaFaseDeGerenciamentoDT(dividaTecnica.getStatusDaFaseDeGerenciamentoDT())
                        .diaDoCadastro(dividaTecnica.getDiaDoCadastro())
                        .idUser(dividaTecnica.getIdUser())
                        .projeto(ProjetoDTO.builder()
                                .nomeDoProjeto(nomeDoProjeto)
                                // Adicione outros campos do ProjetoDTO, se necessário
                                .build())
                        .build());
            }

            return dividasTecnicasDTO;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter todas as DT", e);
        }
    }

    @GetMapping("/esforco-do-pagamento-por-divida")
    public ResponseEntity<Double> obterEsforcoDoPagamentoPorDivida(@RequestParam UUID id) {
        try {
            Optional<DividaTecnica> dividaTecnicaOptional = dividaTecnicaRepository.findById(id);

            if (dividaTecnicaOptional.isPresent()) {
                DividaTecnica dividaTecnica = dividaTecnicaOptional.get();

                // Calcula o esforço de pagamento com base nos atributos da dívida técnica
                Double esforcoDoPagamento = dividaTecnicaService.calcularEsforcoPagamento(dividaTecnica);

                return ResponseEntity.ok(esforcoDoPagamento);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida técnica não encontrada!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DividaTecnica> getDividaTecnicaById(@PathVariable UUID id) {
        try {
            Optional<DividaTecnica> dividaTecnicaOptional = dividaTecnicaRepository.findById(id);
            return dividaTecnicaOptional.map(dividaTecnica -> ResponseEntity.ok().body(dividaTecnica))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // No seu controller
    @PostMapping
    public ResponseEntity salvar(@RequestBody DividaTecnica dividaTecnica, HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            dividaTecnica.setIdUser(idUser);

            // Buscar o Projeto pelo nome
            Optional<Projeto> projetoOptional = projetoRepository.findByNomeDoProjeto(dividaTecnica.getProjeto().getNomeDoProjeto());
            Projeto projeto = projetoOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projeto não encontrado"));

            // Associar o Projeto à DividaTecnica
            dividaTecnica.setProjeto(projeto);

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

    @GetMapping("/status-pagamento")
    public ResponseEntity<Map<String, Long>> obterStatusPagamentoDasDividasTecnicas(HttpServletRequest request) {
        try {

            var idUser = (UUID) request.getAttribute("idUser");
            Map<String, Long> statusPagamentoDT = new HashMap<>();

            for (StatusDoPagamentoDT status : StatusDoPagamentoDT.values()) {
                Long statusDI = dividaTecnicaRepository.countByStatusDoPagamentoDTAndIdUser(status, idUser);
                statusPagamentoDT.put(status.name(), statusDI);
            }

            return ResponseEntity.ok(statusPagamentoDT);
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
                    dividaTecnica.setCausaDaDT(dividaTecnicaAtualizada.getCausaDaDT());
                    dividaTecnica.setEsforcoDoPagamento(dividaTecnica.getEsforcoDoPagamento());
                    dividaTecnica.setProjeto(dividaTecnicaAtualizada.getProjeto());
                    dividaTecnica.setStatusDoPagamentoDT(dividaTecnicaAtualizada.getStatusDoPagamentoDT());
                    dividaTecnica.setStatusDaFaseDeGerenciamentoDT(dividaTecnicaAtualizada.getStatusDaFaseDeGerenciamentoDT());
                    return dividaTecnicaRepository.save(dividaTecnica);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida Técnica não encontrada!") );
    }
}
