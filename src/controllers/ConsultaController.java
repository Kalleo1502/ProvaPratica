package controllers;

import exceptions.SistemaException;
import models.Consulta;
import models.Nutricionista;
import services.ConsultaService;
import views.SistemaView;

import java.util.ArrayList;

public class ConsultaController {
    private final SistemaView view = new SistemaView();
    private final ConsultaService consultaService = new ConsultaService();

    public void iniciar(Nutricionista nutricionista) throws SistemaException {
        int opcao;
        do {
            opcao = view.menu("CONSULTAS E COBRANÇAS", new String[]{
                    "Listar consultas", "Agendar consulta", "Alterar consulta",
                    "Cancelar consulta", "Registrar pagamento", "Voltar"
            });
            switch (opcao) {
                case 0:
                    view.exibir("Consultas", consultaService.listar(nutricionista));
                    break;
                case 1:
                    salvar(nutricionista, null);
                    break;
                case 2:
                    alterar(nutricionista);
                    break;
                case 3:
                    cancelar(nutricionista);
                    break;
                case 4:
                    registrarPagamento(nutricionista);
                    break;
            }
        } while (opcao >= 0 && opcao != 5);
    }

    private void salvar(Nutricionista nutricionista, Consulta consulta) throws SistemaException {
        boolean nova = consulta == null;
        if (nova) consulta = new Consulta(nutricionista, null, null, null, "", 0.0);
        consulta.setNutricionista(nutricionista);
        Consulta dados = view.dadosConsulta(consulta);
        if (dados == null) return;

        boolean salvou = nova ? consultaService.cadastrar(dados) : consultaService.alterar(dados);
        if (salvou) view.sucesso("Consulta", nova ? "Consulta agendada." : "Consulta alterada.");
        else view.erro("Consulta", "Não foi possível salvar a consulta. Verifique o ID do paciente.");
    }

    private void alterar(Nutricionista nutricionista) throws SistemaException {
        ArrayList<Consulta> disponiveis = new ArrayList<>();
        ArrayList<Consulta> todas = consultaService.listarRegistros(nutricionista);
        if (todas != null) {
            for (Consulta consulta : todas) {
                if (!"Cancelada".equalsIgnoreCase(consulta.getStatus())) disponiveis.add(consulta);
            }
        }
        Consulta selecionada = view.escolherOpcao("Alterar consulta",
                "Selecione a consulta que deseja alterar:", disponiveis);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())) {
            view.erro("Consulta", "A consulta não foi encontrada ou já foi cancelada.");
            return;
        }
        salvar(nutricionista, consulta);
    }

    private void cancelar(Nutricionista nutricionista) {
        ArrayList<Consulta> disponiveis = new ArrayList<>();
        ArrayList<Consulta> todas = consultaService.listarRegistros(nutricionista);
        if (todas != null) {
            for (Consulta consulta : todas) {
                if (!"Cancelada".equalsIgnoreCase(consulta.getStatus())) disponiveis.add(consulta);
            }
        }
        Consulta selecionada = view.escolherOpcao("Cancelar consulta",
                "Selecione a consulta que deseja cancelar:", disponiveis);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())) {
            view.erro("Consulta", "A consulta não foi encontrada ou já foi cancelada.");
            return;
        }
        if (view.confirmar("Cancelar consulta", "Deseja cancelar esta consulta?\n\n" + consulta) != 0) return;

        if (consultaService.cancelar(consulta.getId(), nutricionista)) view.sucesso("Consulta", "Consulta cancelada.");
        else view.erro("Consulta", "Não foi possível cancelar a consulta.");
    }

    private void registrarPagamento(Nutricionista nutricionista) {
        ArrayList<Consulta> disponiveis = new ArrayList<>();
        ArrayList<Consulta> todas = consultaService.listarRegistros(nutricionista);
        if (todas != null) {
            for (Consulta consulta : todas) {
                if (!"Cancelada".equalsIgnoreCase(consulta.getStatus())
                        && !"Pago".equalsIgnoreCase(consulta.getStatusPagamento())) disponiveis.add(consulta);
            }
        }
        Consulta selecionada = view.escolherOpcao("Registrar pagamento",
                "Selecione a consulta com pagamento pendente:", disponiveis);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())
                || "Pago".equalsIgnoreCase(consulta.getStatusPagamento())) {
            view.erro("Pagamento", "A consulta não está mais disponível para pagamento.");
            return;
        }
        if (consultaService.pagar(consulta.getId(), nutricionista)) view.sucesso("Pagamento", "Pagamento registrado.");
        else view.erro("Pagamento", "Não foi possível registrar o pagamento.");
    }
}
