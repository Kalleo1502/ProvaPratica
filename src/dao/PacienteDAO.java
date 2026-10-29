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
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.idNutricionista=? AND p.stAtivo=1 ORDER BY p.idPaciente");
            p.setInt(1, nutricionista.getId());
            ResultSet r = p.executeQuery();
            ArrayList<Paciente> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paciente selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.idPaciente=? AND p.idNutricionista=? AND p.stAtivo=1");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paciente login(String email) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE p.nmEmail=? AND p.stAtivo=1 AND n.stAtivo=1");
            p.setString(1, email);
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean inserir(Paciente paciente) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO paciente(idNutricionista,nmNome,nmEmail,nmSenha) VALUES(?,?,?,?)");
            p.setInt(1, paciente.getNutricionista().getId());
            p.setString(2, paciente.getNome());
            p.setString(3, paciente.getEmail());
            p.setString(4, paciente.getSenha());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Paciente paciente) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE paciente SET nmNome=?,nmEmail=?,nmSenha=? WHERE idPaciente=? AND idNutricionista=?");
            p.setString(1, paciente.getNome());
            p.setString(2, paciente.getEmail());
            p.setString(3, paciente.getSenha());
            p.setInt(4, paciente.getId());
            p.setInt(5, paciente.getNutricionista().getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE paciente SET stAtivo=0 WHERE idPaciente=? AND idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
