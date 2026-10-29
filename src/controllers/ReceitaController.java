package controllers;

import exceptions.SistemaException;
import models.Nutricionista;
import models.Receita;
import services.ReceitaService;
import views.SistemaView;

public class ReceitaController {
    private final SistemaView view = new SistemaView();
    private final ReceitaService receitaService = new ReceitaService();

    public void iniciar(Nutricionista nutricionista) throws SistemaException {
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

    private void alterar(Nutricionista nutricionista) throws SistemaException {
        Receita selecionada = view.escolherOpcao("Alterar receita",
                "Selecione a receita que deseja alterar:", receitaService.listarRegistros(nutricionista));
        if (selecionada == null) return;

        Receita receita = receitaService.buscarPorId(selecionada.getId(), nutricionista);
        if (receita == null) {
            view.erro("Receita", "A receita não foi encontrada.");
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
            view.erro("Receita", "A receita não foi encontrada.");
            return;
        }
        if (view.confirmar("Excluir receita", "Deseja excluir esta receita?\n\n" + receita) != 0) return;

        if (receitaService.excluir(receita.getId(), nutricionista)) view.sucesso("Receita", "Receita excluída.");
        else view.erro("Receita", "Não foi possível excluir a receita.");
    }

    private void salvar(Nutricionista nutricionista, Receita receita) throws SistemaException {
        boolean nova = receita == null;
        if (nova) receita = new Receita();
        receita.setNutricionista(nutricionista);

        Receita dados = view.dadosReceita(receita);
        if (dados == null) return;

        boolean salvou = nova ? receitaService.cadastrar(dados) : receitaService.alterar(dados);
        if (salvou) view.sucesso("Receita", "Receita salva com sucesso.");
        else view.erro("Receita", "Não foi possível salvar. Verifique o ID do paciente.");
    }

}
