package services;

import dao.ConsultaDAO;
import dao.PacienteDAO;
import exceptions.SistemaException;
import models.Consulta;
import models.Nutricionista;
import models.Paciente;

import java.util.ArrayList;

public class ConsultaService {
    private final ConsultaDAO consultaDao = new ConsultaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    public Boolean cadastrar(Consulta consulta) throws SistemaException {
        validar(consulta);
        validarRelacionamento(consulta);
        return consultaDao.inserir(consulta);
    }

    public String listar(Nutricionista nutricionista) {
        ArrayList<Consulta> lista = consultaDao.selecionar(nutricionista);
        String texto = "";
        if (lista == null || lista.isEmpty()) return "Nenhuma consulta encontrada.";
        for (Consulta consulta : lista) texto += consulta + "\n";
        return texto;
    }

    public ArrayList<Consulta> listarRegistros(Nutricionista nutricionista) {
        return consultaDao.selecionar(nutricionista);
    }

    public Consulta buscarPorId(Integer id, Nutricionista nutricionista) {
        return consultaDao.selecionarPorId(id, nutricionista);
    }

    public Boolean alterar(Consulta consulta) throws SistemaException {
        validar(consulta);
        if (consulta.getId() == null) throw new SistemaException("Consulta não identificada.");
        validarRelacionamento(consulta);
        return consultaDao.atualizar(consulta);
    }

    public Boolean cancelar(Integer id, Nutricionista nutricionista) {
        return consultaDao.cancelar(id, nutricionista);
    }

    public Boolean pagar(Integer id, Nutricionista nutricionista) {
        return consultaDao.pagar(id, nutricionista);
    }

    private void validarRelacionamento(Consulta consulta) throws SistemaException {
        if (consulta.getNutricionista() == null || consulta.getNutricionista().getId() == null)
            throw new SistemaException("Nutricionista não identificado. Entre novamente no sistema.");
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null)
            throw new SistemaException("ID do paciente obrigatório.");
        Paciente paciente = pacienteDAO.selecionarPorId(consulta.getPaciente().getId(), consulta.getNutricionista());
        if (paciente == null)
            throw new SistemaException("O ID informado não pertence a um paciente ativo deste nutricionista.");
        // Usa o paciente real retornado pelo banco e mantém o mesmo objeto Nutricionista.
        paciente.setNutricionista(consulta.getNutricionista());
        consulta.setPaciente(paciente);
    }

    private void validar(Consulta consulta) throws SistemaException {
        String erros = "";
        if (consulta.getNutricionista() == null || consulta.getNutricionista().getId() == null)
            erros += "\n- Nutricionista não identificado.";
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null)
            erros += "\n- ID do paciente obrigatório.";
        if (consulta.getData() == null) erros += "\n- Data inválida. Use AAAA-MM-DD.";
        if (consulta.getHora() == null) erros += "\n- Horário inválido. Use HH:MM.";
        if (consulta.getValor() == null || consulta.getValor() < 0) erros += "\n- Valor inválido.";
        if (!erros.isEmpty()) throw new SistemaException(erros);
    }
}
