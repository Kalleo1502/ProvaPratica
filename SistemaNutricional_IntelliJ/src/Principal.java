import controllers.NutricionistaController;
import controllers.PacienteController;
import views.SistemaView;

public class Principal {
    private final SistemaView view = new SistemaView();
    private final NutricionistaController nutricionistaController = new NutricionistaController();
    private final PacienteController pacienteController = new PacienteController();

    public static void main(String[] args) {
        new Principal().iniciar();
    }

    private void iniciar() {
        int opcao;
        do {
            opcao = view.menu("SISTEMA DE NUTRIÇÃO", new String[]{
                    "Nutricionista",
                    "Paciente",
                    "Sair"
            });

            switch (opcao) {
                case 0:
                    nutricionistaController.iniciar();
                    break;
                case 1:
                    pacienteController.iniciar();
                    break;
                default:
                    // -1 indica que a janela foi fechada; 2 indica a opção Sair.
                    break;
            }
        } while (opcao >= 0 && opcao != 2);
    }
}
