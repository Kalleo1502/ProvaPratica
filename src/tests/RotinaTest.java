package tests;

import models.Nutricionista;
import models.Paciente;
import models.Rotina;
import services.RotinaService;

public class RotinaTest {

    private final RotinaService rotinaService = new RotinaService();
    // Nutricionista 1 e o paciente 1 dele já existem no banco
    private final Nutricionista nutricionista = new Nutricionista(1, "Teste", "teste@teste", "123", true);
    private final Paciente paciente = new Paciente(1, nutricionista, "Teste", "teste@teste", "123", true);
    private Rotina rotina;

    public void iniciar() {
        System.out.println("=== TESTES ROTINA ===");
        listar();
        cadastrar();
        alterar();
        excluir();
    }

    // Teste automatizado da funcionalidade de listagem
    public void listar() {
        try {
            String lista = rotinaService.listar(nutricionista);
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
            rotina = new Rotina(nutricionista, paciente, "Teste", "Descrição teste", "Segunda");
            Boolean cadastrou = rotinaService.cadastrar(rotina);
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
            rotina.setTitulo("Teste2");
            Boolean alterou = rotinaService.alterar(rotina);
            if(alterou) {
                System.out.println("Alterar: Passou");
            } else {
                System.err.println("Alterar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Alterar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de exclusão
    public void excluir() {
        try {
            Boolean excluiu = rotinaService.excluir(rotina.getId(), nutricionista);
            if(excluiu) {
                System.out.println("Excluir: Passou");
            } else {
                System.err.println("Excluir: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Excluir: Falhou");
        }
    }
}
