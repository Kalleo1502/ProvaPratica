package views;

import models.Consulta;
import models.Paciente;
import utils.Utilidade;

import javax.swing.*;

public class ConsultaView {
    public int menu(String titulo, String[] opcoes) {
        return JOptionPane.showOptionDialog(null, "Selecione uma opção:", titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    public void exibir(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public String ler(String titulo, String texto, Object valor) {
        return (String) JOptionPane.showInputDialog(null, texto, titulo,
                JOptionPane.QUESTION_MESSAGE, null, null, valor);
    }

    public void alertaSucesso(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void alertaErro(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public int alertaConfirmacao(String titulo, String texto) {
        return JOptionPane.showConfirmDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public <T> T escolherOpcao(String titulo, String mensagem, java.util.List<T> opcoes) {
        if (opcoes == null || opcoes.isEmpty()) {
            alertaErro(titulo, "Não há registros disponíveis para selecionar.");
            return null;
        }
        Object selecionado = JOptionPane.showInputDialog(
                null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE,
                null, opcoes.toArray(), opcoes.get(0));

        T resultado = (T) selecionado;
        return resultado;
    }

    public Consulta informarDados(Consulta consulta) {
        Object idAtual = consulta.getPaciente() == null ? null : consulta.getPaciente().getId();
        String id = ler("Consulta", "ID do paciente:", idAtual);
        if (id == null) return null;
        String data = ler("Consulta", "Data (AAAA-MM-DD):", consulta.getData());
        if (data == null) return null;
        String hora = ler("Consulta", "Horário (HH:MM):", consulta.getHora());
        if (hora == null) return null;
        String descricao = ler("Consulta", "Descrição:", consulta.getDescricao());
        if (descricao == null) return null;
        String valor = ler("Consulta", "Valor:", consulta.getValor());
        if (valor == null) return null;
        try {
            Paciente paciente = new Paciente();
            Integer idPaciente = Utilidade.converterInteger(id);
            if (idPaciente == null) throw new IllegalArgumentException();
            paciente.setId(idPaciente);
            consulta.setPaciente(paciente); // Referência provisória, o Service carrega e valida o paciente real.
            java.sql.Date dataConvertida = Utilidade.converterDate(data);
            java.sql.Time horaConvertida = Utilidade.converterTime(hora);
            Double valorConvertido = Utilidade.converterDouble(valor);
            if (dataConvertida == null || horaConvertida == null || valorConvertido == null) {
                throw new IllegalArgumentException();
            }
            consulta.setData(dataConvertida);
            consulta.setHora(horaConvertida);
            consulta.setValor(valorConvertido);
        } catch (Exception e) {
            alertaErro("Consulta", "ID, data, horário ou valor inválido.");
            return null;
        }
        consulta.setDescricao(descricao);
        return consulta;
    }
}
