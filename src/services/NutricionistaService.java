package services;

import dao.NutricionistaDAO;
import exceptions.SistemaException;
import models.Nutricionista;

public class NutricionistaService {
    private final NutricionistaDAO dao = new NutricionistaDAO();

    public Boolean cadastrar(Nutricionista nutricionista) throws SistemaException {
        validar(nutricionista.getNome(), nutricionista.getEmail(), nutricionista.getSenha());
        if (dao.selecionarPorEmail(nutricionista.getEmail()) != null) throw new SistemaException("E-mail já cadastrado.");
        return dao.inserir(nutricionista);
    }

    public Nutricionista login(String email, String senha) {
        Nutricionista nutricionista = dao.selecionarPorEmail(email);
        return nutricionista != null && nutricionista.getSenha().equals(senha) ? nutricionista : null;
    }

    public Nutricionista buscarPorId(Integer id) {
        return dao.selecionarPorId(id);
    }

    public Boolean alterar(Nutricionista nutricionista) throws SistemaException {
        validar(nutricionista.getNome(), nutricionista.getEmail(), nutricionista.getSenha());
        return dao.atualizar(nutricionista);
    }

    public Boolean excluir(Integer id) {
        return dao.deletar(id);
    }

    private void validar(String nome, String email, String senha) throws SistemaException {
        String erros = "";
        if (nome == null || nome.trim().isEmpty()) erros += "\n- Nome obrigatório.";
        if (email == null || !email.contains("@")) erros += "\n- E-mail inválido.";
        if (senha == null || senha.trim().isEmpty()) erros += "\n- Senha obrigatória.";
        if (!erros.isEmpty()) throw new SistemaException(erros);
    }
}
