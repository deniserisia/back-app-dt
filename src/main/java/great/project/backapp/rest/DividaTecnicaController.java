package great.project.backapp.rest;

import great.project.backapp.model.StatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.TipoDeDividaTecnica;
import great.project.backapp.model.dto.DividaTecnicaDTO;
import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import great.project.backapp.repository.DividaTecnicaRepository;
import great.project.backapp.repository.ProjetoRepository;
import great.project.backapp.service.DividaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.JdkIdGenerator;
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

    @GetMapping("/status-gerenciamento/{idUser}")
    public ResponseEntity<Map<String, Long>> obterStatusGerenciamentoDividasTecnicas(@PathVariable(name = "idUser") String idUser) {
        try {
            Map<String, Long> statusFaseGerenDT = new HashMap<>();

            for (StatusDaFaseDeGerenciamentoDT statusGerenciamento : StatusDaFaseDeGerenciamentoDT.values()) {
                Long statusDI = dividaTecnicaRepository.countByStatusDaFaseDeGerenciamentoDTAndIdUser(statusGerenciamento, Long.valueOf(idUser));
                statusFaseGerenDT.put(statusGerenciamento.name(), statusDI);
            }
            return ResponseEntity.ok(statusFaseGerenDT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/todas/{idUser}")
    public List<DividaTecnicaDTO> obterTodas(@PathVariable(name = "idUser") String idUser){
        try {
          // Long idUser = (Long) request.getAttribute("idUser");
            List<DividaTecnica> dividasTecnicas = dividaTecnicaRepository.findByIdUser(Long.valueOf(idUser));

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
  //
    @GetMapping("/esforco-do-pagamento-por-divida/{id}")
    public ResponseEntity<List<DividaTecnica>> obterEsforcoDoPagamentoPorDivida(@PathVariable(name = "id") String id) {
        try {
            Optional<DividaTecnica> dividaTecnicaOptional = dividaTecnicaRepository.findById(Long.valueOf(id));

            if (dividaTecnicaOptional.isPresent()) {
                DividaTecnica dividaTecnica = dividaTecnicaOptional.get();

                // Crie uma lista contendo apenas a divida técnica encontrada
                List<DividaTecnica> dividasTecnicas = new ArrayList<>();
                dividasTecnicas.add(dividaTecnica);

                return ResponseEntity.ok(dividasTecnicas);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida técnica não encontrada!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @GetMapping("/dividas-tecnicas-do-projeto/{id}")
    public ArrayList<DividaTecnica> obterDividasTecnicasDoProjeto(@PathVariable(name = "id") String id) {
        ArrayList<DividaTecnica> list= (ArrayList<DividaTecnica>) dividaTecnicaService.obterDividasTecnicasDoProjeto(Long.valueOf(id));
        return list;
    }
//    public ResponseEntity<List<DividaTecnica>> obterDividasTecnicasDoProjeto(@PathVariable(name = "id") String id) {
//        List<DividaTecnica> dividasTecnicas = dividaTecnicaService.obterDividasTecnicasDoProjeto(Long.valueOf(id));
//        return ResponseEntity.ok(dividasTecnicas);
//    }


    @GetMapping("/{id}")
    public ResponseEntity<DividaTecnica> getDividaTecnicaById(@PathVariable(name = "id") String id) {
        try {
            Optional<DividaTecnica> dividaTecnicaOptional = dividaTecnicaRepository.findById(Long.valueOf(id));
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
          //  Long idUser = dividaTecnica.getIdUser(); //(Long) request.getAttribute("idUser");
          //  dividaTecnica.setIdUser(idUser);

            // Buscar o Projeto pelo no
            Optional<Projeto> projetoOptional = projetoRepository.findById(dividaTecnica.getId_projeto());
            Projeto projeto = projetoOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projeto não encontrado"));

            projeto.getDividasTecnicas().add(dividaTecnica);

            // Associar o Projeto à DividaTecnica
            dividaTecnica.setProjeto(projeto);
            dividaTecnica.setId_projeto(projeto.getId());
            dividaTecnica.setResultadoDoesforcoDoPagammento (dividaTecnica.getValorPorHoraDeTrabalho()* dividaTecnica.getQuantidadeDePessoas());
            projetoRepository.save(projeto);

            //var dividaTecnicaSalva = this.dividaTecnicaRepository.save(dividaTecnica);
            return ResponseEntity.status(HttpStatus.OK).body(dividaTecnica);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a DividaTecnica.");
        }
    }



    @GetMapping("/count/{idUser}")
    public ResponseEntity<Long> obterNumeroDeDividasTecnicasDoUsuario(@PathVariable(name = "idUser") String idUser) {
        try {

            Long numeroDeDT = dividaTecnicaRepository.countByIdUser(Long.valueOf(idUser));
            return ResponseEntity.ok(numeroDeDT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarDT( @PathVariable Long id ){
        dividaTecnicaRepository
                .findById(id)
                .map( dividaTecnica -> {
                    dividaTecnicaRepository.delete(dividaTecnica);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DT não encontrado!") );
    }

    @GetMapping("/contagem-dividas-tecnicas-por-mes-no-ano/{ano}/{idUser}")
    public ResponseEntity<Map<String, Long>> obterContagemDividasTecnicasPorMesNoAno(
            @PathVariable("ano") int ano,
            @PathVariable(name = "idUser") String idUser
    ) {
        try {

            List<DividaTecnica> dividasTecnicas = dividaTecnicaRepository.findByIdUser(Long.valueOf(idUser));

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


    @GetMapping("/contagem-por-tipo/{idUser}")
    public ResponseEntity<Map<String, Long>> obterContagemDividasPorTipo(@PathVariable(name = "idUser") String idUser) {
        try {
            //Long idUser = (Long) request.getAttribute("idUser");
            Map<String, Long> contagemPorTipo = new HashMap<>();

            for (TipoDeDividaTecnica tipo : TipoDeDividaTecnica.values()) {
                Long contagem = dividaTecnicaRepository.countByTipoDeDividaTecnicaAndIdUser(tipo, Long.valueOf(idUser));
                contagemPorTipo.put(tipo.name(), contagem);
            }

            return ResponseEntity.ok(contagemPorTipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status-pagamento/{idUser}")
    public ResponseEntity<Map<String, Long>> obterStatusPagamentoDasDividasTecnicas(@PathVariable(name = "idUser") String idUser) {
        try {
            Map<String, Long> statusPagamentoDT = new HashMap<>();

            for (StatusDoPagamentoDT status : StatusDoPagamentoDT.values()) {
                Long statusDI = dividaTecnicaRepository.countByStatusDoPagamentoDTAndIdUser(status, Long.valueOf(idUser));
                statusPagamentoDT.put(status.name(), statusDI);
            }

            return ResponseEntity.ok(statusPagamentoDT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  atualizarDividaTecnica(@PathVariable(name = "id") Long id, @RequestBody DividaTecnica dividaTecnicaAtualizada, HttpServletRequest request ) {

        Long idprojeto=dividaTecnicaAtualizada.getId_projeto();

        dividaTecnicaRepository
                .findById(id)
                .map( dividaTecnica -> {
                    dividaTecnica.setTipoDeDividaTecnica(dividaTecnicaAtualizada.getTipoDeDividaTecnica());
                    dividaTecnica.setDescricaoDaDT(dividaTecnicaAtualizada.getDescricaoDaDT());
                    dividaTecnica.setCausaDaDT(dividaTecnicaAtualizada.getCausaDaDT());
                    dividaTecnica.setEsforcoDoPagamento(dividaTecnica.getEsforcoDoPagamento());
                    dividaTecnica.setStatusDoPagamentoDT(dividaTecnicaAtualizada.getStatusDoPagamentoDT());
                    dividaTecnica.setStatusDaFaseDeGerenciamentoDT(dividaTecnicaAtualizada.getStatusDaFaseDeGerenciamentoDT());
                    dividaTecnica.setQuantidadeDePessoas(dividaTecnicaAtualizada.getQuantidadeDePessoas());
                    dividaTecnica.setValorPorHoraDeTrabalho(dividaTecnicaAtualizada.getValorPorHoraDeTrabalho());
                    dividaTecnica.setResultadoDoesforcoDoPagammento(dividaTecnicaAtualizada.getValorPorHoraDeTrabalho()* dividaTecnica.getQuantidadeDePessoas());
                    dividaTecnica.setId_projeto(idprojeto);
                    return dividaTecnicaRepository.save(dividaTecnica);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dívida Técnica não encontrada!") );
    }
}
