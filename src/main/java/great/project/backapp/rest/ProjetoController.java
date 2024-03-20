package great.project.backapp.rest;


import great.project.backapp.model.dto.ProjetoDTO;
import great.project.backapp.model.entity.DividaTecnica;
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
import java.time.format.DateTimeFormatter;
import java.util.Map;

import java.time.Month;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/gerente/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;


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



    @GetMapping("/count-pessoas-time-dev")
    public ResponseEntity<Long> obterNumeroDePessoasNoTimeDeDev(HttpServletRequest request) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            Long numeroDePessoas = projetoRepository.sumQuantidadeDePessoasNoTimeDeDevByIdUser(idUser);
            return ResponseEntity.ok(numeroDePessoas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/contagem-projetos-por-mes-no-ano")
    public ResponseEntity<Map<String, Long>> obterContagemProjetosPorMesNoAno(
            @RequestParam("ano") int ano,
            HttpServletRequest request
    ) {
        try {
            var idUser = (UUID) request.getAttribute("idUser");
            List<Projeto> projetos = projetoRepository.findByIdUser(idUser);

            // Filtrar projetos para o ano especificado
            projetos = projetos.stream()
                    .filter(projeto -> projeto.getDataCadastro().getYear() == ano)
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
                    projeto.setStatusProjeto(projetoAtualizado.getStatusProjeto());
                    return projetoRepository.save(projeto);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado!") );
    }
}
