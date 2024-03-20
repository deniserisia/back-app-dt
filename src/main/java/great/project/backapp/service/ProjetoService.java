package great.project.backapp.service;

import great.project.backapp.model.entity.Projeto;
import great.project.backapp.model.entity.Usuario;
import great.project.backapp.repository.ProjetoRepository;
import great.project.backapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public List<Projeto> buscarProjetosPorNomeEEmpresa(String nomeDoProjeto, String empresa) {
        return projetoRepository.findByNomeDoProjetoAndEmpresa(nomeDoProjeto, empresa);
    }

    //public List<Projeto> obterProjetosPorUsuario(UUID idUser) {
    //    return projetoRepository.findByUsuarioId(idUser);
    //}

}
