package great.project.backapp.service;


import great.project.backapp.model.entity.Usuario;
import great.project.backapp.model.entity.UsuarioDTO;
import great.project.backapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PerfilUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO obterUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // throw new UsuarioNaoAutenticadoException("Usuário não autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username);

        // Criar um objeto UsuarioDTO com os dados do usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setNomeDaEmpresa(usuario.getNomeDaEmpresa());
        usuarioDTO.setSetorDaEmpresa(usuario.getSetorDaEmpresa());

        return usuarioDTO;
    }

}
