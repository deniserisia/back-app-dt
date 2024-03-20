package great.project.backapp.rest;

import great.project.backapp.exception.UsuarioCadastradoException;
import great.project.backapp.model.entity.Usuario;
import great.project.backapp.repository.UsuarioRepository;
import great.project.backapp.service.PerfilUsuarioService;
import great.project.backapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final PerfilUsuarioService perfilUsuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid Usuario usuario){
        try {
            usuarioService.salvar(usuario);
        }catch (UsuarioCadastradoException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obterPerfilUsuario() {
        Usuario usuario = perfilUsuarioService.obterUsuarioAutenticado();
        return ResponseEntity.ok(usuario);
    }
}