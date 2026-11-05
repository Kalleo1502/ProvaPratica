package services;

import dao.PacienteDAO;
import dao.RotinaDAO;
import exceptions.RotinaException;
import models.Nutricionista;
import models.Paciente;
import models.Rotina;

import java.util.ArrayList;

public class RotinaService {
    private final RotinaDAO dao = new RotinaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    public Boolean cadastrar(Rotina rotina) throws RotinaException {
        validar(rotina);
        validarRelacionamento(rotina);
        return dao.inserir(rotina);
    }

    public String listar(Nutricionista nutricionista) {
        ArrayList<Rotina> lista = dao.selecionar(nutricionista);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhuma rotina encontrada.";
        for (Rotina r : lista) texto += r + "\n";
        return texto;
    }

    public ArrayList<Rotina> listarRegistros(Nutricionista nutricionista) {
        return dao.selecionar(nutricionista);
    }

    public String listarPorPacienteDia(Paciente paciente, String dia) {
        ArrayList<Rotina> lista = dao.selecionarPorPacienteDia(paciente, dia);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhuma rotina encontrada para " + dia + ".";
        for (Rotina r : lista) texto += r + "\n";
        return texto;
    }

    public Rotina buscarPorId(Integer id, Nutricionista nutricionista) {
        return dao.selecionarPorId(id, nutricionista);
    }

    public Boolean alterar(Rotina rotina) throws RotinaException {
        validar(rotina);
        if (rotina.getId() == null) throw new RotinaException("Rotina não identificada.");
        validarRelacionamento(rotina);
        return dao.atualizar(rotina);
    }

    public Boolean excluir(Integer id, Nutricionista nutricionista) {
        return dao.deletar(id, nutricionista);
    }

    private void validarRelacionamento(Rotina rotina) throws RotinaException {
        if (rotina.getNutricionista() == null || rotina.getNutricionista().getId() == null)
            throw new RotinaException("Nutricionista não identificado. Entre novamente no sistema.");
        if (rotina.getPaciente() == null || rotina.getPaciente().getId() == null)
            throw new RotinaException("ID do paciente obrigatório.");
        Paciente paciente = pacienteDAO.selecionarPorId(rotina.getPaciente().getId(), rotina.getNutricionista());
        if (paciente == null)
            throw new RotinaException("O ID informado não pertence a um paciente ativo deste nutricionista.");
        // Troca a referência provisória pelo paciente completo carregado do banco.
        // Mantém o mesmo objeto Nutricionista nas duas associações.
        paciente.setNutricionista(rotina.getNutricionista());
        rotina.setPaciente(paciente);
    }

    private void validar(Rotina rotina) throws RotinaException {
        String erros = "";
        if (rotina.getTitulo() == null || rotina.getTitulo().trim().isEmpty()) erros += "\n- Título obrigatório.";
        if (rotina.getDescricao() == null || rotina.getDescricao().trim().isEmpty())
            erros += "\n- Descrição obrigatória.";
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        boolean valido = false;
        if (rotina.getDiaSemana() != null)
            for (String dia : dias) if (dia.equalsIgnoreCase(rotina.getDiaSemana())) valido = true;
        if (!valido) erros += "\n- Selecione um dia da semana válido.";
        if (!erros.isEmpty()) throw new RotinaException(erros);
    }
}
