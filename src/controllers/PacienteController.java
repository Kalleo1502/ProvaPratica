package controllers;

import exceptions.SistemaException;
import models.Nutricionista;
import models.Paciente;
import services.PacienteService;
import services.ReceitaService;
import views.SistemaView;

import javax.swing.JOptionPane;

public class PacienteController {
    private final SistemaView view = new SistemaView();
    private final PacienteService pacienteService = new PacienteService();
    private final ReceitaService receitaService = new ReceitaService();

    // Menu de acesso usado pela tela inicial.
    public void iniciar() {
        int opcao;
        do {
            opcao = view.menu("ÁREA DO PACIENTE", new String[]{"Logar-se como Paciente", "Voltar"});
            if (opcao == 0) login();
        } while (opcao >= 0 && opcao != 1);
    }

    private void login() {
        String email = view.ler("Login do paciente", "E-mail:", null);
        if (email == null) return;
        String senha = view.ler("Login do paciente", "Senha:", null);
        if (senha == null) return;

        Paciente paciente = pacienteService.login(email, senha);
        if (paciente == null) {
            view.erro("Login", "E-mail ou senha inválidos.");
            return;
        }
        consultarReceitas(paciente);
    }

    private void consultarReceitas(Paciente paciente) {
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        Object dia;
        do {
            dia = JOptionPane.showInputDialog(null,
                    "Escolha o dia para consultar suas receitas:",
                    "RECEITAS DO PACIENTE", JOptionPane.QUESTION_MESSAGE, null, dias, dias[0]);
            if (dia != null) {
                view.exibir("Receitas - " + dia,
                        receitaService.listarPorPacienteDia(paciente, dia.toString()));
            }
        } while (dia != null);
    }

    // As operações de cadastro são acessadas pelo nutricionista após o login.
    public void gerenciarPacientes(Nutricionista nutricionista) throws SistemaException {
        int opcao;
        do {
            opcao = view.menu("PACIENTES", new String[]{"Listar", "Cadastrar", "Alterar", "Inativar", "Voltar"});
            switch (opcao) {
                case 0:
                    view.exibir("Pacientes", pacienteService.listar(nutricionista));
                    break;
                case 1:
                    cadastrar(nutricionista);
                    break;
                case 2:
                    alterar(nutricionista);
                    break;
                case 3:
                    inativar(nutricionista);
                    break;
            }
        } while (opcao >= 0 && opcao != 4);
    }

    private void cadastrar(Nutricionista nutricionista) throws SistemaException {
        Paciente paciente = view.dadosPaciente(new Paciente(nutricionista, "", "", ""));
        if (paciente == null) return;
        if (pacienteService.cadastrar(paciente)) {
            view.sucesso("Paciente", "Paciente cadastrado. Informe ao paciente o e-mail e a senha.");
        } else {
            view.erro("Paciente", "Não foi possível cadastrar o paciente.");
        }
    }

    private void alterar(Nutricionista nutricionista) throws SistemaException {
        Paciente selecionado = view.escolherOpcao("Alterar paciente",
                "Selecione o paciente que deseja alterar:", pacienteService.listarRegistros(nutricionista));
        if (selecionado == null) return;

        // Busca novamente pelo ID para trabalhar com os dados atuais do banco.
        Paciente paciente = pacienteService.buscarPorId(selecionado.getId(), nutricionista);
        if (paciente == null) {
            view.erro("Paciente", "O paciente não foi encontrado ou não está mais ativo.");
            return;
        }

        Paciente dados = view.dadosPaciente(paciente);
        if (dados == null) return;

        if (pacienteService.alterar(dados)) view.sucesso("Paciente", "Paciente alterado.");
        else view.erro("Paciente", "Não foi possível alterar o paciente.");
    }

    private void inativar(Nutricionista nutricionista) {
        Paciente selecionado = view.escolherOpcao("Inativar paciente",
                "Selecione o paciente que deseja inativar:", pacienteService.listarRegistros(nutricionista));
        if (selecionado == null) return;

        Paciente paciente = pacienteService.buscarPorId(selecionado.getId(), nutricionista);
        if (paciente == null) {
            view.erro("Paciente", "O paciente não foi encontrado ou já está inativo.");
            return;
        }

        String mensagem = "Deseja inativar este paciente?\n\n" + paciente;
        if (view.confirmar("Inativar paciente", mensagem) == 0) {
            if (pacienteService.excluir(paciente.getId(), nutricionista))
                view.sucesso("Paciente", "Paciente inativado.");
            else view.erro("Paciente", "Não foi possível inativar o paciente.");
        }
    }
}
