package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;
import models.Paciente;
import models.Rotina;

import java.sql.*;
import java.util.ArrayList;

public class RotinaDAO {
    private static final String SELECT_BASE =
            "SELECT r.idRotina, r.idNutricionista, r.idPaciente, r.nmTitulo, r.nmDescricao, r.diaSemana, " +
                    "n.nmNome AS nutricionistaNome, n.nmEmail AS nutricionistaEmail, n.nmSenha AS nutricionistaSenha, n.stAtivo AS nutricionistaAtivo, " +
                    "p.nmNome AS pacienteNome, p.nmEmail AS pacienteEmail, p.nmSenha AS pacienteSenha, p.stAtivo AS pacienteAtivo " +
                    "FROM rotina r JOIN nutricionista n ON n.idNutricionista=r.idNutricionista " +
                    "JOIN paciente p ON p.idPaciente=r.idPaciente AND p.idNutricionista=r.idNutricionista ";

    private Rotina mapear(ResultSet r) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(r.getInt("idNutricionista"),
                r.getString("nutricionistaNome"), r.getString("nutricionistaEmail"),
                r.getString("nutricionistaSenha"), r.getBoolean("nutricionistaAtivo"));
        Paciente paciente = new Paciente(r.getInt("idPaciente"), nutricionista,
                r.getString("pacienteNome"), r.getString("pacienteEmail"),
                r.getString("pacienteSenha"), r.getBoolean("pacienteAtivo"));
        return new Rotina(r.getInt("idRotina"), nutricionista, paciente,
                r.getString("nmTitulo"), r.getString("nmDescricao"), r.getString("diaSemana"));
    }

    public ArrayList<Rotina> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idNutricionista=? ORDER BY r.idRotina");
            p.setInt(1, nutricionista.getId());
            ResultSet r = p.executeQuery();
            ArrayList<Rotina> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Rotina> selecionarPorPacienteDia(Paciente paciente, String dia) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idPaciente=? AND r.idNutricionista=? AND r.diaSemana=? ORDER BY r.idRotina");
            p.setInt(1, paciente.getId());
            p.setInt(2, paciente.getNutricionista().getId());
            p.setString(3, dia);
            ResultSet r = p.executeQuery();
            ArrayList<Rotina> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rotina selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE r.idRotina=? AND r.idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código da rotina é sequencial DENTRO de cada nutricionista: cada um começa no 1.
    // O próximo código é o maior código daquele nutricionista + 1.
    private Integer proximoId(Nutricionista nutricionista) throws SQLException {
        PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idRotina) FROM receita WHERE idNutricionista=?");
        p.setInt(1, nutricionista.getId());
        ResultSet r = p.executeQuery();
        r.next();
        return r.getInt(1) + 1; // nutricionista sem rotinas: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Rotina rotina) {
        try {
            Integer id = proximoId(rotina.getNutricionista());
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO rotina(idNutricionista,idRotina,idPaciente,nmTitulo,nmDescricao,diaSemana) VALUES(?,?,?,?,?,?)");
            p.setInt(1, rotina.getNutricionista().getId());
            p.setInt(2, id);
            p.setInt(3, rotina.getPaciente().getId());
            p.setString(4, rotina.getTitulo());
            p.setString(5, rotina.getDescricao());
            p.setString(6, rotina.getDiaSemana());
            boolean inseriu = p.executeUpdate() > 0;
            if (inseriu) rotina.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Rotina rotina) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE rotina SET idPaciente=?,nmTitulo=?,nmDescricao=?,diaSemana=? WHERE idRotina=? AND idNutricionista=?");
            p.setInt(1, rotina.getPaciente().getId());
            p.setString(2, rotina.getTitulo());
            p.setString(3, rotina.getDescricao());
            p.setString(4, rotina.getDiaSemana());
            p.setInt(5, rotina.getId());
            p.setInt(6, rotina.getNutricionista().getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "DELETE FROM rotina WHERE idRotina=? AND idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
