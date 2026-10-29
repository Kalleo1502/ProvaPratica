package views;

import javax.swing.*;

import models.*;
import utils.Utilidade;

public class SistemaView {
    public int menu(String titulo, String[] opcoes) {
        return JOptionPane.showOptionDialog(null, "Selecione uma opção:", titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    public String ler(String titulo, String pergunta, Object valor) {
        return (String) JOptionPane.showInputDialog(null, pergunta, titulo,
                JOptionPane.QUESTION_MESSAGE, null, null, valor);
    }

    public void exibir(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public void sucesso(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void erro(String titulo, String texto) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public int confirmar(String titulo, String texto) {
        return JOptionPane.showConfirmDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public <T> T escolherOpcao(String titulo, String mensagem, java.util.List<T> opcoes) {
        if (opcoes == null || opcoes.isEmpty()) {
            erro(titulo, "Não há registros disponíveis para selecionar.");
            return null;
        }
        Object selecionado = JOptionPane.showInputDialog(
                null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE,
                null, opcoes.toArray(), opcoes.get(0));
        @SuppressWarnings("unchecked")
        T resultado = (T) selecionado;
        return resultado;
    }

    public Nutricionista dadosNutricionista(Nutricionista n) {
        String nome = ler("Nutricionista", "Nome:", n.getNome());
        if (nome == null) return null;
        String email = ler("Nutricionista", "E-mail:", n.getEmail());
        if (email == null) return null;
        String senha = ler("Nutricionista", "Senha:", n.getSenha());
        if (senha == null) return null;
        n.setNome(nome);
        n.setEmail(email);
        n.setSenha(senha);
        return n;
    }

    public Paciente dadosPaciente(Paciente p) {
        String nome = ler("Paciente", "Nome:", p.getNome());
        if (nome == null) return null;
        String email = ler("Paciente", "E-mail:", p.getEmail());
        if (email == null) return null;
        String senha = ler("Paciente", "Senha:", p.getSenha());
        if (senha == null) return null;
        p.setNome(nome);
        p.setEmail(email);
        p.setSenha(senha);
        return p;
    }

    public Receita dadosReceita(Receita r) {
        Object idAtual = r.getPaciente() == null ? null : r.getPaciente().getId();
        String id = ler("Receita", "ID do paciente:", idAtual);
        if (id == null) return null;
        String titulo = ler("Receita", "Título:", r.getTitulo());
        if (titulo == null) return null;
        String descricao = ler("Receita", "Descrição:", r.getDescricao());
        if (descricao == null) return null;
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        Object diaAtual = r.getDiaSemana() == null ? dias[0] : r.getDiaSemana();
        Object dia = JOptionPane.showInputDialog(null, "Dia da semana:", "Receita",
                JOptionPane.QUESTION_MESSAGE, null, dias, diaAtual);
        if (dia == null) return null;
        try {
            Paciente paciente = new Paciente();
            Integer idPaciente = Utilidade.converterInteger(id);
            if (idPaciente == null) throw new IllegalArgumentException();
            paciente.setId(idPaciente);
            r.setPaciente(paciente); // Referência provisória; o Service carrega e valida o paciente real.
        } catch (Exception e) {
            erro("Receita", "ID de paciente inválido.");
            return null;
        }
        r.setTitulo(titulo);
        r.setDescricao(descricao);
        r.setDiaSemana(dia.toString());
        return r;
    }

    public Consulta dadosConsulta(Consulta c) {
        Object idAtual = c.getPaciente() == null ? null : c.getPaciente().getId();
        String id = ler("Consulta", "ID do paciente:", idAtual);
        if (id == null) return null;
        String data = ler("Consulta", "Data (AAAA-MM-DD):", c.getData());
        if (data == null) return null;
        String hora = ler("Consulta", "Horário (HH:MM):", c.getHora());
        if (hora == null) return null;
        String descricao = ler("Consulta", "Descrição:", c.getDescricao());
        if (descricao == null) return null;
        String valor = ler("Consulta", "Valor:", c.getValor());
        if (valor == null) return null;
        try {
            Paciente paciente = new Paciente();
            Integer idPaciente = Utilidade.converterInteger(id);
            if (idPaciente == null) throw new IllegalArgumentException();
            paciente.setId(idPaciente);
            c.setPaciente(paciente); // Referência provisória; o Service carrega e valida o paciente real.
            java.sql.Date dataConvertida = Utilidade.converterDate(data);
            java.sql.Time horaConvertida = Utilidade.converterTime(hora);
            Double valorConvertido = Utilidade.converterDouble(valor);
            if (dataConvertida == null || horaConvertida == null || valorConvertido == null) {
                throw new IllegalArgumentException();
            }
            c.setData(dataConvertida);
            c.setHora(horaConvertida);
            c.setValor(valorConvertido);
        } catch (Exception e) {
            erro("Consulta", "ID, data, horário ou valor inválido.");
            return null;
        }
        c.setDescricao(descricao);
        return c;
    }
}
