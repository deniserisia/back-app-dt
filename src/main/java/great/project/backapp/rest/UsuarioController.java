package great.project.backapp.rest;

import at.favre.lib.crypto.bcrypt.BCrypt;
import great.project.backapp.exception.UsuarioCadastradoException;
import great.project.backapp.model.entity.Usuario;
import great.project.backapp.repository.UsuarioRepository;
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

    private final UsuarioService service;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/cadastro")
    public ResponseEntity create(@RequestBody Usuario usuario){
        var user = this.usuarioRepository.findByUsername(usuario.getUsername());
        if (user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Escolha outro username, por favor, esse já existe!");
        }

        // cripto bd - password
        var passwordHashred = BCrypt.withDefaults().hashToString(12 , usuario.getPassword().toCharArray());
        usuario.setPassword(passwordHashred);

        var userCreated = this.usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
    }
}