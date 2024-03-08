package great.project.backapp.service;

import great.project.backapp.repository.DividaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DividaTecnicaService {

    @Autowired
    private DividaTecnicaRepository dividaTecnicaRepository;
}
