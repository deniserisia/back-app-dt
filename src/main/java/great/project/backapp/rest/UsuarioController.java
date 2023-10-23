package great.project.backapp.rest;

import at.favre.lib.crypto.bcrypt.BCrypt;
import great.project.backapp.model.entity.Usuario;
import great.project.backapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> obterTodos(){
        return usuarioRepository.findAll();
    }

    @PostMapping("/cadastro-de-usuarios")
    public ResponseEntity create(@RequestBody Usuario usuario){
        var userr = this.usuarioRepository.findByUsername(usuario.getUsername());
        if (userr != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Escolha outro username, por favor, esse já existe!");
        }

        // cripto bd - password
        var passwordHashred = BCrypt.withDefaults().hashToString(12 , usuario.getPassword().toCharArray());
        usuario.setPassword(passwordHashred);

        var userCreated = this.usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
    }

}
