import tests.ConsultaTest;
import tests.NutricionistaTest;
import tests.PacienteTest;
import tests.ReceitaTest;

public class Teste {
    public static void main(String[] args) {
        NutricionistaTest nutricionistaTest = new NutricionistaTest();
        nutricionistaTest.iniciar();

        PacienteTest pacienteTest = new PacienteTest();
        pacienteTest.iniciar();

        ReceitaTest receitaTest = new ReceitaTest();
        receitaTest.iniciar();

        ConsultaTest consultaTest = new ConsultaTest();
        consultaTest.iniciar();
    }
}
