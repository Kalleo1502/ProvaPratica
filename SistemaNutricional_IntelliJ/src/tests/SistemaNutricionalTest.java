package tests;

import exceptions.SistemaException;
import models.Consulta;
import models.Nutricionista;
import models.Paciente;
import models.Receita;
import services.ConsultaService;
import services.NutricionistaService;
import services.PacienteService;
import services.ReceitaService;

public class SistemaNutricionalTest {
    private final NutricionistaService nutricionistaService = new NutricionistaService();
    private final PacienteService pacienteService = new PacienteService();
    private final ReceitaService receitaService = new ReceitaService();
    private final ConsultaService consultaService = new ConsultaService();

    public void iniciar() {
        System.out.println("=== TESTES DO SISTEMA NUTRICIONAL ===");
        testarValidacaoNutricionista();
        testarValidacaoPaciente();
        testarValidacaoReceita();
        testarValidacaoConsulta();
    }

    public void testarValidacaoNutricionista() {
        try {
            nutricionistaService.cadastrar(new Nutricionista("", "email-invalido", ""));
            System.err.println("Validação Nutricionista: Falhou");
        } catch (SistemaException e) {
            System.out.println("Validação Nutricionista: Passou");
        }
    }

    public void testarValidacaoPaciente() {
        try {
            pacienteService.cadastrar(new Paciente(null, "", "email-invalido", ""));
            System.err.println("Validação Paciente: Falhou");
        } catch (SistemaException e) {
            System.out.println("Validação Paciente: Passou");
        }
    }

    public void testarValidacaoReceita() {
        try {
            receitaService.cadastrar(new Receita(null, null, "", "", ""));
            System.err.println("Validação Receita: Falhou");
        } catch (SistemaException e) {
            System.out.println("Validação Receita: Passou");
        }
    }

    public void testarValidacaoConsulta() {
        try {
            consultaService.cadastrar(new Consulta());
            System.err.println("Validação Consulta: Falhou");
        } catch (SistemaException e) {
            System.out.println("Validação Consulta: Passou");
        }
    }
}
