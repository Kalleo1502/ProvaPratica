package tests;

import models.Nutricionista;
import models.Paciente;
import services.PacienteService;

public class PacienteTest {

    private final PacienteService pacienteService = new PacienteService();
    // Nutricionista 1 já existe no banco
    private final Nutricionista nutricionista = new Nutricionista(1, "Teste", "teste@teste", "123", true);
    private Paciente paciente;

    public void iniciar() {
        System.out.println("=== TESTES PACIENTE ===");
        listar();
        cadastrar();
        alterar();
        excluir();
    }

    // Teste automatizado da funcionalidade de listagem
    public void listar() {
        try {
            String lista = pacienteService.listar(nutricionista);
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
            paciente = new Paciente(nutricionista, "Teste", "teste" + System.currentTimeMillis() + "@teste", "123");
            Boolean cadastrou = pacienteService.cadastrar(paciente);
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
            paciente.setNome("Teste2");
            Boolean alterou = pacienteService.alterar(paciente);
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
            Boolean excluiu = pacienteService.excluir(paciente.getId(), nutricionista);
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
