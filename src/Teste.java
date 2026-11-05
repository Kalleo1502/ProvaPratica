import tests.ConsultaTest;
import tests.NutricionistaTest;
import tests.PacienteTest;
import tests.RotinaTest;

public class Teste {
    public static void main(String[] args) {
        NutricionistaTest nutricionistaTest = new NutricionistaTest();
        nutricionistaTest.iniciar();

        PacienteTest pacienteTest = new PacienteTest();
        pacienteTest.iniciar();

        RotinaTest rotinaTest = new RotinaTest();
        rotinaTest.iniciar();

        ConsultaTest consultaTest = new ConsultaTest();
        consultaTest.iniciar();
    }
}
