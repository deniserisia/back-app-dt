package great.project.backapp.rest;

import great.project.backapp.model.StatusProjeto;
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
import java.util.*;
import java.util.stream.Collectors;
import java.time.Month;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gerente/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;


    @GetMapping("/todos/{idUser}")
    public List<Projeto> obterTodos(@PathVariable(name = "idUser") String idUser) {
        try {

            return projetoRepository.findByIdUser(Long.valueOf(idUser));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter todos os projetos do usuário", e);
        }
    }

    @GetMapping("/todosprojetos/{idUser}")
    public List<Projeto> obterTodosDoUsuario(@PathVariable(name = "idUser") String idUser){
        try {
            return projetoRepository.findByIdUser(Long.valueOf(idUser));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter todos os projetos do usuário", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> obterProjetoPorId(@PathVariable Long id) {
        try {
            Optional<Projeto> projetoOptional = projetoRepository.findById(id);
            return projetoOptional.map(projeto -> ResponseEntity.ok().body(projeto))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para obter o status do projeto
    @GetMapping("/status/{idUser}")
    public ResponseEntity<Map<String, Long>> obterStatusDoProjeto(@PathVariable(name = "idUser") String idUser) {
        try {
            List<Projeto> projetos = projetoRepository.findByIdUser(Long.valueOf(idUser));

            // Contagem dos projetos por status
            Map<StatusProjeto, Long> contagemPorStatus = new HashMap<>();
            for (Projeto projeto : projetos) {
                StatusProjeto statusProjeto = projeto.getStatusProjeto();
                contagemPorStatus.put(statusProjeto, contagemPorStatus.getOrDefault(statusProjeto, 0L) + 1);
            }

            // Convertendo para o formato desejado para o gráfico
            Map<String, Long> resultado = new HashMap<>();
            for (Map.Entry<StatusProjeto, Long> entry : contagemPorStatus.entrySet()) {
                resultado.put(entry.getKey().name(), entry.getValue());
            }

            return ResponseEntity.ok(resultado);
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
    public ResponseEntity<?> pesquisarProjetosBD(
            @RequestParam(value = "nomeDoProjeto", required = false, defaultValue = "") String nomeDoProjeto,
            @RequestParam(value = "empresa", required = false, defaultValue = "") String empresa
    ) {
        // Verifica se ambos os parâmetros estão vazios
        if (nomeDoProjeto.isEmpty() && empresa.isEmpty()) {
            // Se ambos estiverem vazios, retorne uma mensagem de erro
            return ResponseEntity.badRequest().body("Especifique o nome do projeto ou a empresa.");
        }

        // Lista para armazenar os resultados da pesquisa
        List<Projeto> projetos = new ArrayList<>();

        // Realiza a pesquisa de acordo com os parâmetros fornecidos
        if (!nomeDoProjeto.isEmpty() && empresa.isEmpty()) {
            projetos = projetoRepository.findByNomeDoProjetoContainingIgnoreCase(nomeDoProjeto);
        } else if (nomeDoProjeto.isEmpty() && !empresa.isEmpty()) {
            projetos = projetoRepository.findByEmpresaContainingIgnoreCase(empresa);
        } else {
            projetos = projetoRepository.findByNomeDoProjetoContainingIgnoreCaseAndEmpresaContainingIgnoreCase(nomeDoProjeto, empresa);
        }

        // Verifica se foram encontrados projetos
        if (!projetos.isEmpty()) {
            // Se foram encontrados, retorna uma mensagem de sucesso e os resultados
            return ResponseEntity.ok("A busca foi realizada com sucesso. Existe um projeto e uma empresa como você pesquisou: " + projetos);
        } else {
            // Se não foram encontrados, retorna uma mensagem indicando que nenhum resultado foi encontrado
            return ResponseEntity.ok("A busca não encontrou nenhum resultado.");
        }
    }




    @PostMapping
    public ResponseEntity salvar(@RequestBody Projeto projeto) {
        //Long idUser = (Long) request.getAttribute("idUser");
        //projeto.setIdUser(idUser);
        var project = this.projetoRepository.save(projeto);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }


    @GetMapping("/count/{idUser}")
    public ResponseEntity<Long> obterNumeroDeProjetosDoUsuario(@PathVariable(name = "idUser") String idUser) {
        try {

            Long numeroDeProjetos = projetoRepository.countByIdUser(Long.valueOf(idUser));
            return ResponseEntity.ok(numeroDeProjetos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/count-pessoas-time-dev")
    public ResponseEntity<Long> obterNumeroDePessoasNoTimeDeDev(HttpServletRequest request) {
        try {
            Long idUser = (Long) request.getAttribute("idUser");
            Long numeroDePessoas = projetoRepository.sumQuantidadeDePessoasNoTimeDeDevByIdUser(idUser);
            return ResponseEntity.ok(numeroDePessoas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/contagem-projetos-por-mes-no-ano/{ano}/{idUser}" )
    public ResponseEntity<Map<String, Long>> obterContagemProjetosPorMesNoAno(
            @PathVariable(name = "ano") Long ano,
            @PathVariable(name="idUser") String idUser
    ) {
        try {

            List<Projeto> projetos = projetoRepository.findByIdUser(Long.valueOf(idUser));

            // Filtrar projetos para o ano especificado
            projetos = projetos.stream()
                    .filter(projeto -> {
                        return projeto.getDataCadastro().getYear() == ano;
                    })
                    .collect(Collectors.toList());

            // Criar uma lista com os nomes dos meses
            String[] nomesDosMeses = {
                    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            };

            // Agrupar projetos por mês
            Map<Month, Long> contagemPorMes = new HashMap<>();
            for (Month month : Month.values()) {
                long count = projetos.stream()
                        .filter(projeto -> projeto.getDataCadastro().getMonth() == month)
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



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProjeto( @PathVariable Long id ){
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

    public void atualizarProjeto( @PathVariable(name = "id") Long id, @RequestBody Projeto projetoAtualizado ) {
        projetoRepository
                .findById(id)
                .map( projeto -> {
                    projeto.setNomeDoProjeto(projetoAtualizado.getNomeDoProjeto());
                    projeto.setNomeDoLiderDoProjeto(projetoAtualizado.getNomeDoLiderDoProjeto());
                    projeto.setEmpresa(projetoAtualizado.getEmpresa());
                    projeto.setDescricao(projetoAtualizado.getDescricao());
                    projeto.setSetor(projetoAtualizado.getSetor());
                    projeto.setStatusProjeto(projetoAtualizado.getStatusProjeto());
                    return projetoRepository.save(projeto);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado!") );
    }
}
