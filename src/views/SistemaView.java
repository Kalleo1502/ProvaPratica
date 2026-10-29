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

    public Nutricionista dadosNutricionista(Nutricionista nutricionista) {
        String nome = ler("Nutricionista", "Nome:", nutricionista.getNome());
        if (nome == null) return null;
        String email = ler("Nutricionista", "E-mail:", nutricionista.getEmail());
        if (email == null) return null;
        String senha = ler("Nutricionista", "Senha:", nutricionista.getSenha());
        if (senha == null) return null;
        nutricionista.setNome(nome);
        nutricionista.setEmail(email);
        nutricionista.setSenha(senha);
        return nutricionista;
    }

    public Paciente dadosPaciente(Paciente paciente) {
        String nome = ler("Paciente", "Nome:", paciente.getNome());
        if (nome == null) return null;
        String email = ler("Paciente", "E-mail:", paciente.getEmail());
        if (email == null) return null;
        String senha = ler("Paciente", "Senha:", paciente.getSenha());
        if (senha == null) return null;
        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setSenha(senha);
        return paciente;
    }

    public Receita dadosReceita(Receita receita) {
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
            erro("Receita", "ID de paciente inválido.");
            return null;
        }
        receita.setTitulo(titulo);
        receita.setDescricao(descricao);
        receita.setDiaSemana(dia.toString());
        return receita;
    }

    public Consulta dadosConsulta(Consulta consulta) {
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
            erro("Consulta", "ID, data, horário ou valor inválido.");
            return null;
        }
        consulta.setDescricao(descricao);
        return consulta;
    }
}
