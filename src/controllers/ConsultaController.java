package controllers;

import exceptions.ConsultaException;
import models.Consulta;
import models.Nutricionista;
import services.ConsultaService;
import views.ConsultaView;

import java.util.ArrayList;

public class ConsultaController {
    private final ConsultaView view = new ConsultaView();
    private final ConsultaService consultaService = new ConsultaService();
    public ArrayList<Consulta> consultas = new ArrayList<>();

    public void iniciar(Nutricionista nutricionista) throws ConsultaException {
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

    private void salvar(Nutricionista nutricionista, Consulta consulta) throws ConsultaException {
        boolean nova = consulta == null;
        if (nova) consulta = new Consulta(nutricionista, null, null, null, "", 0.0);
        consulta.setNutricionista(nutricionista);
        Consulta dados = view.informarDados(consulta);
        if (dados == null) return;
        boolean salvou = nova ? consultaService.cadastrar(dados) : consultaService.alterar(dados);
        if (salvou) view.alertaSucesso("Consulta", nova ? "Consulta agendada." : "Consulta alterada.");
        else view.alertaErro("Consulta", "Não foi possível salvar a consulta. Verifique o ID do paciente.");
    }

    private void alterar(Nutricionista nutricionista) throws ConsultaException {
        consultas = consultasNaoCanceladas(nutricionista);
        Consulta selecionada = view.escolherOpcao("Alterar consulta",
                "Selecione a consulta que deseja alterar:", consultas);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())) {
            view.alertaErro("Consulta", "A consulta não foi encontrada ou já foi cancelada.");
            return;
        }
        salvar(nutricionista, consulta);
    }

    private void cancelar(Nutricionista nutricionista) {
        consultas = consultasNaoCanceladas(nutricionista);
        Consulta selecionada = view.escolherOpcao("Cancelar consulta",
                "Selecione a consulta que deseja cancelar:", consultas);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())) {
            view.alertaErro("Consulta", "A consulta não foi encontrada ou já foi cancelada.");
            return;
        }
        if (view.alertaConfirmacao("Cancelar consulta", "Deseja cancelar esta consulta?\n\n" + consulta) != 0) return;

        if (consultaService.cancelar(consulta.getId(), nutricionista)) view.alertaSucesso("Consulta", "Consulta cancelada.");
        else view.alertaErro("Consulta", "Não foi possível cancelar a consulta.");
    }

    private void registrarPagamento(Nutricionista nutricionista) {
        consultas = consultasComPagamentoPendente(nutricionista);
        Consulta selecionada = view.escolherOpcao("Registrar pagamento",
                "Selecione a consulta com pagamento pendente:", consultas);
        if (selecionada == null) return;

        Consulta consulta = consultaService.buscarPorId(selecionada.getId(), nutricionista);
        if (consulta == null || "Cancelada".equalsIgnoreCase(consulta.getStatus())
                || "Pago".equalsIgnoreCase(consulta.getStatusPagamento())) {
            view.alertaErro("Pagamento", "A consulta não está mais disponível para pagamento.");
            return;
        }
        if (consultaService.pagar(consulta.getId(), nutricionista)) view.alertaSucesso("Pagamento", "Pagamento registrado.");
        else view.alertaErro("Pagamento", "Não foi possível registrar o pagamento.");
    }

    // Monta uma lista NOVA só com as consultas que não foram canceladas.
    // (Antes o código percorria a lista e adicionava itens nela mesma, o que lança
    // ConcurrentModificationException - uma exceção sem mensagem, que gerava o pop-up de erro vazio.)
    private ArrayList<Consulta> consultasNaoCanceladas(Nutricionista nutricionista) {
        ArrayList<Consulta> filtradas = new ArrayList<>();
        ArrayList<Consulta> todas = consultaService.listarRegistros(nutricionista);
        if (todas != null) {
            for (Consulta consulta : todas) {
                if (!"Cancelada".equalsIgnoreCase(consulta.getStatus())) filtradas.add(consulta);
            }
        }
        return filtradas;
    }

    // Consultas não canceladas e ainda sem pagamento registrado.
    private ArrayList<Consulta> consultasComPagamentoPendente(Nutricionista nutricionista) {
        ArrayList<Consulta> filtradas = new ArrayList<>();
        for (Consulta consulta : consultasNaoCanceladas(nutricionista)) {
            if (!"Pago".equalsIgnoreCase(consulta.getStatusPagamento())) filtradas.add(consulta);
        }
        return filtradas;
    }
}
