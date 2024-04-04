package great.project.backapp.service;

import great.project.backapp.model.StatusDaFaseDeGerenciamentoDT;
import great.project.backapp.model.StatusDoPagamentoDT;
import great.project.backapp.model.entity.DividaTecnica;
import great.project.backapp.model.entity.Projeto;
import great.project.backapp.repository.DividaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DividaTecnicaService {

    @Autowired
    private DividaTecnicaRepository dividaTecnicaRepository;


    public void cadastrarDividaTecnica(String descricaoDaDT, Projeto projeto, Double esforcoDoPagamento,
                                       String causaDaDT, Double valorPorHoraDeTrabalho, Integer quantidadeDePessoas,
                                       StatusDoPagamentoDT statusDoPagamentoDT, StatusDaFaseDeGerenciamentoDT statusDaFaseDeGerenciamentoDT,
                                       LocalDateTime diaDoCadastro, UUID idUser) {
        DividaTecnica dividaTecnica = DividaTecnica.builder()
                .descricaoDaDT(descricaoDaDT)
                .projeto(projeto)
                .esforcoDoPagamento(esforcoDoPagamento)
                .causaDaDT(causaDaDT)
                .valorPorHoraDeTrabalho(valorPorHoraDeTrabalho)
                .quantidadeDePessoas(quantidadeDePessoas)
                .statusDoPagamentoDT(statusDoPagamentoDT)
                .statusDaFaseDeGerenciamentoDT(statusDaFaseDeGerenciamentoDT)
                .diaDoCadastro(diaDoCadastro)
                .idUser(idUser)
                .build();

        Double resultadoDoEsforcoDoPagamento = calcularEsforcoPagamento(dividaTecnica);

        System.out.println("Resultado do Esforço de Pagamento: R$: " + resultadoDoEsforcoDoPagamento);
    }

    //Calculo de pessoas vs horas trabalhadas
    public Double calcularEsforcoPagamento(DividaTecnica dividaTecnica) {
        return dividaTecnica.getQuantidadeDePessoas() * dividaTecnica.getValorPorHoraDeTrabalho();
    }
}
