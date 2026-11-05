package tests;

import models.Nutricionista;
import services.NutricionistaService;

public class NutricionistaTest {

    private final NutricionistaService nutricionistaService = new NutricionistaService();
    private final String email = "teste" + System.currentTimeMillis() + "@teste";
    private Nutricionista nutricionista;

    public void iniciar() {
        System.out.println("=== TESTES NUTRICIONISTA ===");
        cadastrar();
        login();
        alterar();
        excluir();
    }

    // Teste automatizado da funcionalidade de cadastro
    public void cadastrar() {
        try {
            Boolean cadastrou = nutricionistaService.cadastrar(new Nutricionista("Teste", email, "123"));
            if(cadastrou) {
                System.out.println("Cadastrar: Passou");
            } else {
                System.err.println("Cadastrar: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Cadastrar: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de login
    public void login() {
        try {
            nutricionista = nutricionistaService.login(email, "123");
            if(nutricionista != null) {
                System.out.println("Login: Passou");
            } else {
                System.err.println("Login: Falhou");
            }
        } catch (Exception e) {
            System.err.println("Login: Falhou");
        }
    }

    // Teste automatizado da funcionalidade de alteração
    public void alterar() {
        try {
            nutricionista.setNome("Teste2");
            Boolean alterou = nutricionistaService.alterar(nutricionista);
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
            Boolean excluiu = nutricionistaService.excluir(nutricionista.getId());
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
