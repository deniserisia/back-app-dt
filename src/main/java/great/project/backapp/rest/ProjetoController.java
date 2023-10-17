package great.project.backapp.rest;

import great.project.backapp.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;
}
