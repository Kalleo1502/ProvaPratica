package controllers;

import exceptions.RotinaException;
import models.Nutricionista;
import models.Rotina;
import services.RotinaService;
import views.RotinaView;

public class RotinaController {
    private final RotinaView view = new RotinaView();
    private final RotinaService rotinaService = new RotinaService();

    public void iniciar(Nutricionista nutricionista) throws RotinaException {
        int opcao;
        do {
            opcao = view.menu("ROTINAS", new String[]{"Listar", "Cadastrar", "Alterar", "Excluir", "Voltar"});
            switch (opcao) {
                case 0:
                    view.exibir("Rotinas", rotinaService.listar(nutricionista));
                    break;
                case 1:
                    salvar(nutricionista, null);
                    break;
                case 2:
                    alterar(nutricionista);
                    break;
                case 3:
                    excluir(nutricionista);
                    break;
            }
        } while (opcao >= 0 && opcao != 4);
    }

    private void alterar(Nutricionista nutricionista) throws RotinaException {
        Rotina selecionada = view.escolherOpcao("Alterar rotina",
                "Selecione a rotina que deseja alterar:", rotinaService.listarRegistros(nutricionista));
        if (selecionada == null) return;

        Rotina rotina = rotinaService.buscarPorId(selecionada.getId(), nutricionista);
        if (rotina == null) {
            view.alertaErro("Rotina", "A rotina não foi encontrada.");
            return;
        }
        salvar(nutricionista, rotina);
    }

    private void excluir(Nutricionista nutricionista) {
        Rotina selecionada = view.escolherOpcao("Excluir rotina",
                "Selecione a rotina que deseja excluir:", rotinaService.listarRegistros(nutricionista));
        if (selecionada == null) return;

        Rotina rotina = rotinaService.buscarPorId(selecionada.getId(), nutricionista);
        if (rotina == null) {
            view.alertaErro("Rotina", "A rotina não foi encontrada.");
            return;
        }
        if (view.alertaConfirmacao("Excluir rotina", "Deseja excluir esta rotina?\n\n" + rotina) != 0) return;

        if (rotinaService.excluir(rotina.getId(), nutricionista)) view.alertaSucesso("Rotina", "Rotina excluída.");
        else view.alertaErro("Rotina", "Não foi possível excluir a rotina.");
    }

    private void salvar(Nutricionista nutricionista, Rotina rotina) throws RotinaException {
        boolean nova = rotina == null;
        if (nova) rotina = new Rotina();
        rotina.setNutricionista(nutricionista);

        Rotina dados = view.informarDados(rotina);
        if (dados == null) return;

        boolean salvou = nova ? rotinaService.cadastrar(dados) : rotinaService.alterar(dados);
        if (salvou) view.alertaSucesso("Rotina", "Rotina salva com sucesso.");
        else view.alertaErro("Rotina", "Não foi possível salvar. Verifique o ID do paciente.");
    }

}
