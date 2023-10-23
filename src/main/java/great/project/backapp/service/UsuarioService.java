package great.project.backapp.service;


import great.project.backapp.model.UsuarioRoles;
import great.project.backapp.model.entity.Usuario;
import great.project.backapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService{


    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        }

   // public List<Usuario> findAdminUsers() {
   //     return usuarioRepository.findByRolesContaining(UsuarioRoles.USER_ADMIN);
   // }
}

