package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;
import models.Paciente;

import java.sql.*;
import java.util.ArrayList;

public class PacienteDAO {
    private static final String SELECT_BASE =
            "SELECT pac.idPaciente, pac.idNutricionista, pac.nmNome AS pacienteNome, " +
                    "pac.nmEmail AS pacienteEmail, pac.nmSenha AS pacienteSenha, pac.stAtivo AS pacienteAtivo, " +
                    "nutri.nmNome AS nutricionistaNome, nutri.nmEmail AS nutricionistaEmail, " +
                    "nutri.nmSenha AS nutricionistaSenha, nutri.stAtivo AS nutricionistaAtivo " +
                    "FROM paciente pac JOIN nutricionista nutri ON nutri.idNutricionista=pac.idNutricionista ";

    private Paciente mapear(ResultSet resultado) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(resultado.getInt("idNutricionista"),
                resultado.getString("nutricionistaNome"), resultado.getString("nutricionistaEmail"),
                resultado.getString("nutricionistaSenha"), resultado.getBoolean("nutricionistaAtivo"));
        return new Paciente(resultado.getInt("idPaciente"), nutricionista, resultado.getString("pacienteNome"),
                resultado.getString("pacienteEmail"), resultado.getString("pacienteSenha"), resultado.getBoolean("pacienteAtivo"));
    }

    public ArrayList<Paciente> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE pac.idNutricionista=? AND pac.stAtivo=1 ORDER BY pac.idPaciente");
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
                    SELECT_BASE + "WHERE pac.idPaciente=? AND pac.idNutricionista=? AND pac.stAtivo=1");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Paciente login(String email) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE pac.nmEmail=? AND pac.stAtivo=1 AND nutri.stAtivo=1");
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
