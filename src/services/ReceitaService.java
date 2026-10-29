package services;

import dao.PacienteDAO;
import dao.ReceitaDAO;
import exceptions.SistemaException;
import models.Nutricionista;
import models.Paciente;
import models.Receita;

import java.util.ArrayList;

public class ReceitaService {
    private final ReceitaDAO dao = new ReceitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    public Boolean cadastrar(Receita receita) throws SistemaException {
        validar(receita);
        validarRelacionamento(receita);
        return dao.inserir(receita);
    }

    public String listar(Nutricionista nutricionista) {
        ArrayList<Receita> lista = dao.selecionar(nutricionista);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhuma receita encontrada.";
        for (Receita r : lista) texto += r + "\n";
        return texto;
    }

    public ArrayList<Receita> listarRegistros(Nutricionista nutricionista) {
        return dao.selecionar(nutricionista);
    }

    public String listarPorPacienteDia(Paciente paciente, String dia) {
        ArrayList<Receita> lista = dao.selecionarPorPacienteDia(paciente, dia);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhuma receita encontrada para " + dia + ".";
        for (Receita r : lista) texto += r + "\n";
        return texto;
    }

    public Receita buscarPorId(Integer id, Nutricionista nutricionista) {
        return dao.selecionarPorId(id, nutricionista);
    }

    public Boolean alterar(Receita receita) throws SistemaException {
        validar(receita);
        if (receita.getId() == null) throw new SistemaException("Receita não identificada.");
        validarRelacionamento(receita);
        return dao.atualizar(receita);
    }

    public Boolean excluir(Integer id, Nutricionista nutricionista) {
        return dao.deletar(id, nutricionista);
    }

    private void validarRelacionamento(Receita receita) throws SistemaException {
        if (receita.getNutricionista() == null || receita.getNutricionista().getId() == null)
            throw new SistemaException("Nutricionista não identificado. Entre novamente no sistema.");
        if (receita.getPaciente() == null || receita.getPaciente().getId() == null)
            throw new SistemaException("ID do paciente obrigatório.");
        Paciente paciente = pacienteDAO.selecionarPorId(receita.getPaciente().getId(), receita.getNutricionista());
        if (paciente == null)
            throw new SistemaException("O ID informado não pertence a um paciente ativo deste nutricionista.");
        // Troca a referência provisória pelo paciente completo carregado do banco.
        // Mantém o mesmo objeto Nutricionista nas duas associações.
        paciente.setNutricionista(receita.getNutricionista());
        receita.setPaciente(paciente);
    }

    private void validar(Receita receita) throws SistemaException {
        String erros = "";
        if (receita.getTitulo() == null || receita.getTitulo().trim().isEmpty()) erros += "\n- Título obrigatório.";
        if (receita.getDescricao() == null || receita.getDescricao().trim().isEmpty())
            erros += "\n- Descrição obrigatória.";
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        boolean valido = false;
        if (receita.getDiaSemana() != null)
            for (String dia : dias) if (dia.equalsIgnoreCase(receita.getDiaSemana())) valido = true;
        if (!valido) erros += "\n- Selecione um dia da semana válido.";
        if (!erros.isEmpty()) throw new SistemaException(erros);
    }
}
