package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;
import models.Paciente;
import models.Receita;

import java.sql.*;
import java.util.ArrayList;

public class ReceitaDAO {
    private static final String SELECT_BASE =
            "SELECT r.idReceita, r.idNutricionista, r.idPaciente, r.nmTitulo, r.nmDescricao, r.diaSemana, " +
                    "n.nmNome AS nutricionistaNome, n.nmEmail AS nutricionistaEmail, n.nmSenha AS nutricionistaSenha, n.stAtivo AS nutricionistaAtivo, " +
                    "p.nmNome AS pacienteNome, p.nmEmail AS pacienteEmail, p.nmSenha AS pacienteSenha, p.stAtivo AS pacienteAtivo " +
                    "FROM receita r JOIN nutricionista n ON n.idNutricionista=r.idNutricionista " +
                    "JOIN paciente p ON p.idPaciente=r.idPaciente AND p.idNutricionista=r.idNutricionista ";

    private Receita mapear(ResultSet r) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(r.getInt("idNutricionista"),
                r.getString("nutricionistaNome"), r.getString("nutricionistaEmail"),
                r.getString("nutricionistaSenha"), r.getBoolean("nutricionistaAtivo"));
        Paciente paciente = new Paciente(r.getInt("idPaciente"), nutricionista,
                r.getString("pacienteNome"), r.getString("pacienteEmail"),
                r.getString("pacienteSenha"), r.getBoolean("pacienteAtivo"));
        return new Receita(r.getInt("idReceita"), nutricionista, paciente,
                r.getString("nmTitulo"), r.getString("nmDescricao"), r.getString("diaSemana"));
    }

    public ArrayList<Receita> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idNutricionista=? ORDER BY r.idReceita");
            p.setInt(1, nutricionista.getId());
            ResultSet r = p.executeQuery();
            ArrayList<Receita> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Receita> selecionarPorPacienteDia(Paciente paciente, String dia) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idPaciente=? AND r.idNutricionista=? AND r.diaSemana=? ORDER BY r.idReceita");
            p.setInt(1, paciente.getId());
            p.setInt(2, paciente.getNutricionista().getId());
            p.setString(3, dia);
            ResultSet r = p.executeQuery();
            ArrayList<Receita> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Receita selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idReceita=? AND r.idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean inserir(Receita receita) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO receita(idNutricionista,idPaciente,nmTitulo,nmDescricao,diaSemana) VALUES(?,?,?,?,?)");
            p.setInt(1, receita.getNutricionista().getId());
            p.setInt(2, receita.getPaciente().getId());
            p.setString(3, receita.getTitulo());
            p.setString(4, receita.getDescricao());
            p.setString(5, receita.getDiaSemana());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Receita receita) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE receita SET idPaciente=?,nmTitulo=?,nmDescricao=?,diaSemana=? WHERE idReceita=? AND idNutricionista=?");
            p.setInt(1, receita.getPaciente().getId());
            p.setString(2, receita.getTitulo());
            p.setString(3, receita.getDescricao());
            p.setString(4, receita.getDiaSemana());
            p.setInt(5, receita.getId());
            p.setInt(6, receita.getNutricionista().getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "DELETE FROM receita WHERE idReceita=? AND idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
