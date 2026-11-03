package controllers;

import exceptions.ReceitaException;
import models.Nutricionista;
import models.Receita;
import services.ReceitaService;
import views.ReceitaView;

public class ReceitaController {
    private final ReceitaView view = new ReceitaView();
    private final ReceitaService receitaService = new ReceitaService();

    public void iniciar(Nutricionista nutricionista) throws ReceitaException {
        int opcao;
        do {
            opcao = view.menu("RECEITAS", new String[]{"Listar", "Cadastrar", "Alterar", "Excluir", "Voltar"});
            switch (opcao) {
                case 0:
                    view.exibir("Receitas", receitaService.listar(nutricionista));
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

    private void alterar(Nutricionista nutricionista) throws ReceitaException {
        Receita selecionada = view.escolherOpcao("Alterar receita",
                "Selecione a receita que deseja alterar:", receitaService.listarRegistros(nutricionista));
        if (selecionada == null) return;

        Receita receita = receitaService.buscarPorId(selecionada.getId(), nutricionista);
        if (receita == null) {
            view.alertaErro("Receita", "A receita não foi encontrada.");
            return;
        }
        salvar(nutricionista, receita);
    }

    private void excluir(Nutricionista nutricionista) {
        Receita selecionada = view.escolherOpcao("Excluir receita",
                "Selecione a receita que deseja excluir:", receitaService.listarRegistros(nutricionista));
        if (selecionada == null) return;

        Receita receita = receitaService.buscarPorId(selecionada.getId(), nutricionista);
        if (receita == null) {
            view.alertaErro("Receita", "A receita não foi encontrada.");
            return;
        }
        if (view.alertaConfirmacao("Excluir receita", "Deseja excluir esta receita?\n\n" + receita) != 0) return;

        if (receitaService.excluir(receita.getId(), nutricionista)) view.alertaSucesso("Receita", "Receita excluída.");
        else view.alertaErro("Receita", "Não foi possível excluir a receita.");
    }

    private void salvar(Nutricionista nutricionista, Receita receita) throws ReceitaException {
        boolean nova = receita == null;
        if (nova) receita = new Receita();
        receita.setNutricionista(nutricionista);

        Receita dados = view.informarDados(receita);
        if (dados == null) return;

        boolean salvou = nova ? receitaService.cadastrar(dados) : receitaService.alterar(dados);
        if (salvou) view.alertaSucesso("Receita", "Receita salva com sucesso.");
        else view.alertaErro("Receita", "Não foi possível salvar. Verifique o ID do paciente.");
    }

}
