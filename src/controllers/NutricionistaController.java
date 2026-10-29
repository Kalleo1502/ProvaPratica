package controllers;

import exceptions.SistemaException;
import models.Nutricionista;
import services.NutricionistaService;
import views.SistemaView;

public class NutricionistaController {
    private final SistemaView view = new SistemaView();
    private final NutricionistaService nutricionistaService = new NutricionistaService();
    private final PacienteController pacienteController = new PacienteController();
    private final ReceitaController receitaController = new ReceitaController();
    private final ConsultaController consultaController = new ConsultaController();

    public void iniciar() {
        int opcao;
        do {
            opcao = view.menu("ÁREA DO NUTRICIONISTA", new String[]{
                    "Logar-se como Nutricionista",
                    "Cadastrar-se como Nutricionista",
                    "Voltar"
            });
            try {
                switch (opcao) {
                    case 0:
                        login();
                        break;
                    case 1:
                        cadastrar();
                        break;
                }
            } catch (Exception e) {
                view.erro("Nutricionista", e.getMessage());
            }
        } while (opcao >= 0 && opcao != 2);
    }

    private void cadastrar() throws SistemaException {
        Nutricionista nutricionista = view.dadosNutricionista(new Nutricionista());
        if (nutricionista == null) return;

        if (nutricionistaService.cadastrar(nutricionista)) {
            view.sucesso("Cadastro", "Nutricionista cadastrado com sucesso!");
        } else {
            view.erro("Cadastro", "Não foi possível cadastrar o nutricionista.");
        }
    }

    private void login() {
        String email = view.ler("Login do nutricionista", "E-mail:", null);
        if (email == null) return;
        String senha = view.ler("Login do nutricionista", "Senha:", null);
        if (senha == null) return;

        Nutricionista nutricionista = nutricionistaService.login(email, senha);
        if (nutricionista == null) {
            view.erro("Login", "E-mail ou senha inválidos.");
            return;
        }
        menuPrincipal(nutricionista);
    }

    private void menuPrincipal(Nutricionista nutricionista) {
        int opcao;
        do {
            opcao = view.menu("NUTRICIONISTA - " + nutricionista.getNome(), new String[]{
                    "Pacientes", "Receitas", "Consultas e cobranças", "Meu cadastro", "Sair"
            });
            try {
                switch (opcao) {
                    case 0:
                        pacienteController.gerenciarPacientes(nutricionista);
                        break;
                    case 1:
                        receitaController.iniciar(nutricionista);
                        break;
                    case 2:
                        consultaController.iniciar(nutricionista);
                        break;
                    case 3:
                        // Retorna ao menu inicial se o próprio nutricionista inativar a conta.
                        if (gerenciarCadastro(nutricionista)) return;
                        break;
                }
            } catch (Exception e) {
                view.erro("Nutricionista", e.getMessage());
            }
        } while (opcao >= 0 && opcao != 4);
    }

    /**
     * Gerencia o cadastro do próprio nutricionista.
     * Retorna true quando a conta é inativada para encerrar a sessão atual.
     */
    private boolean gerenciarCadastro(Nutricionista nutricionista) throws SistemaException {
        int opcao;
        do {
            opcao = view.menu("MEU CADASTRO", new String[]{
                    "Consultar meu cadastro",
                    "Alterar meu cadastro",
                    "Inativar meu cadastro",
                    "Voltar"
            });

            switch (opcao) {
                case 0: {
                    Nutricionista atual = nutricionistaService.buscarPorId(nutricionista.getId());
                    if (atual == null) {
                        view.erro("Meu cadastro", "Não foi possível localizar seu cadastro.");
                    } else {
                        view.exibir("Meu cadastro", atual.toString());
                    }
                    break;
                }
                case 1: {
                    Nutricionista atual = nutricionistaService.buscarPorId(nutricionista.getId());
                    if (atual == null) {
                        view.erro("Meu cadastro", "Não foi possível localizar seu cadastro.");
                        break;
                    }

                    Nutricionista dados = view.dadosNutricionista(atual);
                    if (dados == null) break;

                    if (nutricionistaService.alterar(dados)) {
                        // Atualiza o objeto da sessão para que o menu reflita os novos dados.
                        nutricionista.setNome(dados.getNome());
                        nutricionista.setEmail(dados.getEmail());
                        nutricionista.setSenha(dados.getSenha());
                        view.sucesso("Meu cadastro", "Dados alterados com sucesso.");
                    } else {
                        view.erro("Meu cadastro", "Não foi possível alterar os dados.");
                    }
                    break;
                }
                case 2: {
                    int confirmacao = view.confirmar("Inativar cadastro",
                            "Deseja realmente inativar sua conta?\n\n" +
                                    "Você será desconectado e não poderá entrar novamente com essa conta.");
                    if (confirmacao != 0) break;

                    if (nutricionistaService.excluir(nutricionista.getId())) {
                        view.sucesso("Meu cadastro", "Conta inativada. Você será desconectado.");
                        return true;
                    }
                    view.erro("Meu cadastro", "Não foi possível inativar a conta.");
                    break;
                }
            }
        } while (opcao >= 0 && opcao != 3);
        return false;
    }
}
