package views;

import models.Consulta;
import models.Nutricionista;
import models.Paciente;
import models.Receita;
import utils.Utilidade;

import javax.swing.*;

public class ReceitaView {
    public int menu(String titulo, String[] opcoes) {
        return JOptionPane.showOptionDialog(null, "Selecione uma opção:", titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    public void exibir(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public String ler(String titulo, String pergunta, Object valor) {
        return (String) JOptionPane.showInputDialog(null, pergunta, titulo,
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

    public Receita informarDados(Receita receita) {
        Object idAtual = receita.getPaciente() == null ? null : receita.getPaciente().getId();
        String id = ler("Receita", "ID do paciente:", idAtual);
        if (id == null) return null;
        String titulo = ler("Receita", "Título:", receita.getTitulo());
        if (titulo == null) return null;
        String descricao = ler("Receita", "Descrição:", receita.getDescricao());
        if (descricao == null) return null;
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        Object diaAtual = receita.getDiaSemana() == null ? dias[0] : receita.getDiaSemana();
        Object dia = JOptionPane.showInputDialog(null, "Dia da semana:", "Receita",
                JOptionPane.QUESTION_MESSAGE, null, dias, diaAtual);
        if (dia == null) return null;
        try {
            Paciente paciente = new Paciente();
            Integer idPaciente = Utilidade.converterInteger(id);
            if (idPaciente == null) throw new IllegalArgumentException();
            paciente.setId(idPaciente);
            receita.setPaciente(paciente); // Referência provisória, o Service carrega e valida o paciente real.
        } catch (Exception e) {
            alertaErro("Receita", "ID de paciente inválido.");
            return null;
        }
        receita.setTitulo(titulo);
        receita.setDescricao(descricao);
        receita.setDiaSemana(dia.toString());
        return receita;
    }
}
