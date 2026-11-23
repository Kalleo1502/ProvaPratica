package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;
import models.Paciente;
import models.Rotina;

import java.sql.*;
import java.util.ArrayList;

public class RotinaDAO {
    private static final String SELECT_BASE =
            "SELECT rot.idRotina, rot.idNutricionista, rot.idPaciente, rot.nmTitulo, rot.nmDescricao, rot.diaSemana, " +
                    "nutri.nmNome AS nutricionistaNome, nutri.nmEmail AS nutricionistaEmail, nutri.nmSenha AS nutricionistaSenha, nutri.stAtivo AS nutricionistaAtivo, " +
                    "pac.nmNome AS pacienteNome, pac.nmEmail AS pacienteEmail, pac.nmSenha AS pacienteSenha, pac.stAtivo AS pacienteAtivo " +
                    "FROM rotina rot JOIN nutricionista nutri ON nutri.idNutricionista=rot.idNutricionista " +
                    "JOIN paciente pac ON pac.idPaciente=rot.idPaciente AND pac.idNutricionista=rot.idNutricionista ";

    private Rotina mapear(ResultSet resultado) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(resultado.getInt("idNutricionista"),
                resultado.getString("nutricionistaNome"), resultado.getString("nutricionistaEmail"),
                resultado.getString("nutricionistaSenha"), resultado.getBoolean("nutricionistaAtivo"));
        Paciente paciente = new Paciente(resultado.getInt("idPaciente"), nutricionista,
                resultado.getString("pacienteNome"), resultado.getString("pacienteEmail"),
                resultado.getString("pacienteSenha"), resultado.getBoolean("pacienteAtivo"));
        return new Rotina(resultado.getInt("idRotina"), nutricionista, paciente,
                resultado.getString("nmTitulo"), resultado.getString("nmDescricao"), resultado.getString("diaSemana"));
    }

    public ArrayList<Rotina> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE rot.idNutricionista=? ORDER BY rot.idRotina");
            preparacao.setInt(1, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            ArrayList<Rotina> lista = new ArrayList<>();
            while (resultado.next()) lista.add(mapear(resultado));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Rotina> selecionarPorPacienteDia(Paciente paciente, String dia) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE rot.idPaciente=? AND rot.idNutricionista=? AND rot.diaSemana=? ORDER BY rot.idRotina");
            preparacao.setInt(1, paciente.getId());
            preparacao.setInt(2, paciente.getNutricionista().getId());
            preparacao.setString(3, dia);
            ResultSet resultado = preparacao.executeQuery();
            ArrayList<Rotina> lista = new ArrayList<>();
            while (resultado.next()) lista.add(mapear(resultado));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rotina selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE rot.idRotina=? AND rot.idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código da rotina é sequencial DENTRO de cada nutricionista: cada um começa no 1.
    // O próximo código é o maior código daquele nutricionista + 1.
    private Integer proximoId(Nutricionista nutricionista) throws SQLException {
        PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idRotina) FROM rotina WHERE idNutricionista=?");
        preparacao.setInt(1, nutricionista.getId());
        ResultSet resultado = preparacao.executeQuery();
        resultado.next();
        return resultado.getInt(1) + 1; // nutricionista sem rotinas: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Rotina rotina) {
        try {
            Integer id = proximoId(rotina.getNutricionista());
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO rotina(idNutricionista,idRotina,idPaciente,nmTitulo,nmDescricao,diaSemana) VALUES(?,?,?,?,?,?)");
            preparacao.setInt(1, rotina.getNutricionista().getId());
            preparacao.setInt(2, id);
            preparacao.setInt(3, rotina.getPaciente().getId());
            preparacao.setString(4, rotina.getTitulo());
            preparacao.setString(5, rotina.getDescricao());
            preparacao.setString(6, rotina.getDiaSemana());
            boolean inseriu = preparacao.executeUpdate() > 0;
            if (inseriu) rotina.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Rotina rotina) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE rotina SET idPaciente=?,nmTitulo=?,nmDescricao=?,diaSemana=? WHERE idRotina=? AND idNutricionista=?");
            preparacao.setInt(1, rotina.getPaciente().getId());
            preparacao.setString(2, rotina.getTitulo());
            preparacao.setString(3, rotina.getDescricao());
            preparacao.setString(4, rotina.getDiaSemana());
            preparacao.setInt(5, rotina.getId());
            preparacao.setInt(6, rotina.getNutricionista().getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "DELETE FROM rotina WHERE idRotina=? AND idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
