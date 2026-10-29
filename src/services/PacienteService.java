package services;

import dao.PacienteDAO;
import exceptions.SistemaException;
import models.Nutricionista;
import models.Paciente;

import java.util.ArrayList;

public class PacienteService {
    private final PacienteDAO dao = new PacienteDAO();

    public Boolean cadastrar(Paciente paciente) throws SistemaException {
        validar(paciente);
        if (paciente.getNutricionista() == null || paciente.getNutricionista().getId() == null)
            throw new SistemaException("Nutricionista não identificado.");
        return dao.inserir(paciente);
    }

    public String listar(Nutricionista nutricionista) {
        ArrayList<Paciente> lista = dao.selecionar(nutricionista);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhum paciente encontrado.";
        for (Paciente paciente : lista) texto += paciente + "\n";
        return texto;
    }

    public ArrayList<Paciente> listarRegistros(Nutricionista nutricionista) {
        return dao.selecionar(nutricionista);
    }

    public Paciente buscarPorId(Integer id, Nutricionista nutricionista) {
        return dao.selecionarPorId(id, nutricionista);
    }

    public Boolean alterar(Paciente paciente) throws SistemaException {
        validar(paciente);
        if (paciente.getId() == null || paciente.getNutricionista() == null || paciente.getNutricionista().getId() == null)
            throw new SistemaException("Paciente ou nutricionista não identificado.");
        return dao.atualizar(paciente);
    }

    public Boolean excluir(Integer id, Nutricionista nutricionista) {
        return dao.deletar(id, nutricionista);
    }

    public Paciente login(String email, String senha) {
        Paciente paciente = dao.login(email);
        return paciente != null && paciente.getSenha().equals(senha) ? paciente : null;
    }

    private void validar(Paciente paciente) throws SistemaException {
        String erros = "";
        if (paciente.getNome() == null || paciente.getNome().trim().isEmpty()) erros += "\n- Nome obrigatório.";
        if (paciente.getEmail() == null || !paciente.getEmail().contains("@")) erros += "\n- E-mail inválido.";
        if (paciente.getSenha() == null || paciente.getSenha().trim().isEmpty()) erros += "\n- Senha obrigatória.";
        if (!erros.isEmpty()) throw new SistemaException(erros);
    }
}
