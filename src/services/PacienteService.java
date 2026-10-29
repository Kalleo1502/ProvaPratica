package services;

import dao.PacienteDAO;
import exceptions.SistemaException;
import models.Nutricionista;
import models.Paciente;

import java.util.ArrayList;

public class PacienteService {
    private final PacienteDAO dao = new PacienteDAO();

    public Boolean cadastrar(Paciente p) throws SistemaException {
        validar(p);
        if (p.getNutricionista() == null || p.getNutricionista().getId() == null)
            throw new SistemaException("Nutricionista não identificado.");
        return dao.inserir(p);
    }

    public String listar(Nutricionista nutricionista) {
        ArrayList<Paciente> lista = dao.selecionar(nutricionista);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhum paciente encontrado.";
        for (Paciente p : lista) texto += p + "\n";
        return texto;
    }

    public ArrayList<Paciente> listarRegistros(Nutricionista nutricionista) {
        return dao.selecionar(nutricionista);
    }

    public Paciente buscarPorId(Integer id, Nutricionista nutricionista) {
        return dao.selecionarPorId(id, nutricionista);
    }

    public Boolean alterar(Paciente p) throws SistemaException {
        validar(p);
        if (p.getId() == null || p.getNutricionista() == null || p.getNutricionista().getId() == null)
            throw new SistemaException("Paciente ou nutricionista não identificado.");
        return dao.atualizar(p);
    }

    public Boolean excluir(Integer id, Nutricionista nutricionista) {
        return dao.deletar(id, nutricionista);
    }

    public Paciente login(String email, String senha) {
        Paciente p = dao.login(email);
        return p != null && p.getSenha().equals(senha) ? p : null;
    }

    private void validar(Paciente p) throws SistemaException {
        String erros = "";
        if (p.getNome() == null || p.getNome().trim().isEmpty()) erros += "\n- Nome obrigatório.";
        if (p.getEmail() == null || !p.getEmail().contains("@")) erros += "\n- E-mail inválido.";
        if (p.getSenha() == null || p.getSenha().trim().isEmpty()) erros += "\n- Senha obrigatória.";
        if (!erros.isEmpty()) throw new SistemaException(erros);
    }
}
