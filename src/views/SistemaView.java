package views;

import controllers.NutricionistaController;
import controllers.PacienteController;

import javax.swing.*;

public class SistemaView {
    private final NutricionistaController nutricionistaController = new NutricionistaController();
    private final PacienteController pacienteController = new PacienteController();

    public int menu(String titulo, String[] opcoes) {
        return JOptionPane.showOptionDialog(null, "Selecione uma opção:", titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    public void iniciar() {
        int opcao;
        do {
            opcao = menu("SISTEMA DE NUTRIÇÃO", new String[]{
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
