package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;
import models.Paciente;

import java.sql.*;
import java.util.ArrayList;

public class PacienteDAO {
    private static final String SELECT_BASE =
            "SELECT p.idPaciente, p.idNutricionista, p.nmNome AS pacienteNome, " +
                    "p.nmEmail AS pacienteEmail, p.nmSenha AS pacienteSenha, p.stAtivo AS pacienteAtivo, " +
                    "n.nmNome AS nutricionistaNome, n.nmEmail AS nutricionistaEmail, " +
                    "n.nmSenha AS nutricionistaSenha, n.stAtivo AS nutricionistaAtivo " +
                    "FROM paciente p JOIN nutricionista n ON n.idNutricionista=p.idNutricionista ";

    private Paciente mapear(ResultSet r) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(r.getInt("idNutricionista"),
                r.getString("nutricionistaNome"), r.getString("nutricionistaEmail"),
                r.getString("nutricionistaSenha"), r.getBoolean("nutricionistaAtivo"));
        return new Paciente(r.getInt("idPaciente"), nutricionista, r.getString("pacienteNome"),
                r.getString("pacienteEmail"), r.getString("pacienteSenha"), r.getBoolean("pacienteAtivo"));
    }

    public ArrayList<Paciente> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.idNutricionista=? AND p.stAtivo=1 ORDER BY p.idPaciente");
            preparacao.setInt(1, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            ArrayList<Paciente> lista = new ArrayList<>();
            while (resultado.next()) lista.add(mapear(resultado));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paciente selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.idPaciente=? AND p.idNutricionista=? AND p.stAtivo=1");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            ResultSet r = preparacao.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paciente login(String email) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.nmEmail=? AND p.stAtivo=1 AND n.stAtivo=1");
            preparacao.setString(1, email);
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código do paciente é sequencial DENTRO de cada nutricionista: cada um começa no 1.
    // O próximo código é o maior código daquele nutricionista + 1.
    private Integer proximoId(Nutricionista nutricionista) throws SQLException {
        PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idPaciente) FROM paciente WHERE idNutricionista=?");
        preparacao.setInt(1, nutricionista.getId());
        ResultSet resultado = preparacao.executeQuery();
        resultado.next();
        return resultado.getInt(1) + 1; // nutricionista sem pacientes: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Paciente paciente) {
        try {
            Integer id = proximoId(paciente.getNutricionista());
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO paciente(idNutricionista,idPaciente,nmNome,nmEmail,nmSenha,stAtivo) VALUES(?,?,?,?,?,1)");
            preparacao.setInt(1, paciente.getNutricionista().getId());
            preparacao.setInt(2, id);
            preparacao.setString(3, paciente.getNome());
            preparacao.setString(4, paciente.getEmail());
            preparacao.setString(5, paciente.getSenha());
            boolean inseriu = preparacao.executeUpdate() > 0;
            if (inseriu) paciente.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Paciente paciente) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE paciente SET nmNome=?,nmEmail=?,nmSenha=? WHERE idPaciente=? AND idNutricionista=?");
            preparacao.setString(1, paciente.getNome());
            preparacao.setString(2, paciente.getEmail());
            preparacao.setString(3, paciente.getSenha());
            preparacao.setInt(4, paciente.getId());
            preparacao.setInt(5, paciente.getNutricionista().getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE paciente SET stAtivo=0 WHERE idPaciente=? AND idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
