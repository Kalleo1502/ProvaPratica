package tests;

import models.Consulta;
import models.Nutricionista;
import models.Paciente;
import services.ConsultaService;

import java.sql.Date;
import java.sql.Time;

public class ConsultaTest {

    private final ConsultaService consultaService = new ConsultaService();
    // Nutricionista 1 e o paciente 1 dele já existem no banco
    private final Nutricionista nutricionista = new Nutricionista(1, "Teste", "teste@teste", "123", true);
    private final Paciente paciente = new Paciente(1, nutricionista, "Teste", "teste@teste", "123", true);
    private Consulta consulta;

    public void iniciar() {
        System.out.println("=== TESTES CONSULTA ===");
        listar();
        cadastrar();
        alterar();
        pagar();
        cancelar();
    }

    // Teste automatizado da funcionalidade de listagem
    public void listar() {
        try {
            String lista = consultaService.listar(nutricionista);
            if(!lista.isEmpty()) {
                System.out.println("Listar: Passou");
            } else {
                System.err.println("Listar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Listar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de cadastro
    public void cadastrar() {
        try {
            consulta = new Consulta(nutricionista, paciente,
                    Date.valueOf("2026-12-01"), Time.valueOf("10:00:00"), "Teste", 100.0);
            Boolean cadastrou = consultaService.cadastrar(consulta);
            if(cadastrou) {
                System.out.println("Cadastrar: Passou");
            } else {
                System.err.println("Cadastrar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Cadastrar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de alteração
    public void alterar() {
        try {
            consulta.setDescricao("Teste2");
            Boolean alterou = consultaService.alterar(consulta);
            if(alterou) {
                System.out.println("Alterar: Passou");
            } else {
                System.err.println("Alterar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Alterar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de pagamento
    public void pagar() {
        try {
            Boolean pagou = consultaService.pagar(consulta.getId(), nutricionista);
            if(pagou) {
                System.out.println("Pagar: Passou");
            } else {
                System.err.println("Pagar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Pagar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de cancelamento (a consulta não é apagada, só cancelada)
    public void cancelar() {
        try {
            Boolean cancelou = consultaService.cancelar(consulta.getId(), nutricionista);
            if(cancelou) {
                System.out.println("Cancelar: Passou");
            } else {
                System.err.println("Cancelar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Cancelar: Falhou");
        }
    }
}
