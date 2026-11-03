package views;

import models.Nutricionista;

import javax.swing.*;

public class NutricionistaView {
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

    public Nutricionista informarDados(Nutricionista nutricionista) {
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
}
