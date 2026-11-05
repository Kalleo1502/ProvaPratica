package dao;

import connections.ConexaoMySQL;
import models.Consulta;
import models.Nutricionista;
import models.Paciente;

import java.sql.*;
import java.util.ArrayList;

public class ConsultaDAO {
    private static final String SELECT_BASE =
            "SELECT c.idConsulta, c.idNutricionista, c.idPaciente, c.dtConsulta, c.hrConsulta, c.dsConsulta, " +
                    "c.stConsulta, c.valorConsulta, c.stPagamento, " +
                    "n.nmNome AS nutricionistaNome, n.nmEmail AS nutricionistaEmail, n.nmSenha AS nutricionistaSenha, n.stAtivo AS nutricionistaAtivo, " +
                    "p.nmNome AS pacienteNome, p.nmEmail AS pacienteEmail, p.nmSenha AS pacienteSenha, p.stAtivo AS pacienteAtivo " +
                    "FROM consulta c JOIN nutricionista n ON n.idNutricionista=c.idNutricionista " +
                    "JOIN paciente p ON p.idPaciente=c.idPaciente AND p.idNutricionista=c.idNutricionista ";

    private Consulta mapear(ResultSet r) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(r.getInt("idNutricionista"),
                r.getString("nutricionistaNome"), r.getString("nutricionistaEmail"),
                r.getString("nutricionistaSenha"), r.getBoolean("nutricionistaAtivo"));
        Paciente paciente = new Paciente(r.getInt("idPaciente"), nutricionista,
                r.getString("pacienteNome"), r.getString("pacienteEmail"),
                r.getString("pacienteSenha"), r.getBoolean("pacienteAtivo"));
        return new Consulta(r.getInt("idConsulta"), nutricionista, paciente, r.getDate("dtConsulta"),
                r.getTime("hrConsulta"), r.getString("dsConsulta"), r.getString("stConsulta"),
                r.getDouble("valorConsulta"), r.getString("stPagamento"));
    }

    public ArrayList<Consulta> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE c.idNutricionista=? ORDER BY c.dtConsulta,c.hrConsulta");
            p.setInt(1, nutricionista.getId());
            ResultSet r = p.executeQuery();
            ArrayList<Consulta> lista = new ArrayList<>();
            while (r.next()) lista.add(mapear(r));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Consulta selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE c.idConsulta=? AND c.idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            ResultSet r = p.executeQuery();
            return r.next() ? mapear(r) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código da consulta é sequencial DENTRO de cada nutricionista: cada um começa no 1.
    // O próximo código é o maior código daquele nutricionista + 1.
    private Integer proximoId(Nutricionista nutricionista) throws SQLException {
        PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idConsulta) FROM consulta WHERE idNutricionista=?");
        p.setInt(1, nutricionista.getId());
        ResultSet r = p.executeQuery();
        r.next();
        return r.getInt(1) + 1; // nutricionista sem consultas: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Consulta consulta) {
        try {
            Integer id = proximoId(consulta.getNutricionista());
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO consulta(idNutricionista,idConsulta,idPaciente,dtConsulta,hrConsulta,dsConsulta,valorConsulta,stConsulta,stPagamento) VALUES(?,?,?,?,?,?,?,?,?)");
            p.setInt(1, consulta.getNutricionista().getId());
            p.setInt(2, id);
            p.setInt(3, consulta.getPaciente().getId());
            p.setDate(4, consulta.getData());
            p.setTime(5, consulta.getHora());
            p.setString(6, consulta.getDescricao());
            p.setDouble(7, consulta.getValor());
            p.setString(8, consulta.getStatus());
            p.setString(9, consulta.getStatusPagamento());
            boolean inseriu = p.executeUpdate() > 0;
            if (inseriu) consulta.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Consulta consulta) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET idPaciente=?,dtConsulta=?,hrConsulta=?,dsConsulta=?,stConsulta=?,valorConsulta=?,stPagamento=? WHERE idConsulta=? AND idNutricionista=?");
            p.setInt(1, consulta.getPaciente().getId());
            p.setDate(2, consulta.getData());
            p.setTime(3, consulta.getHora());
            p.setString(4, consulta.getDescricao());
            p.setString(5, consulta.getStatus());
            p.setDouble(6, consulta.getValor());
            p.setString(7, consulta.getStatusPagamento());
            p.setInt(8, consulta.getId());
            p.setInt(9, consulta.getNutricionista().getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean cancelar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET stConsulta='Cancelada' WHERE idConsulta=? AND idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean pagar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement p = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET stPagamento='Pago' WHERE idConsulta=? AND idNutricionista=?");
            p.setInt(1, id);
            p.setInt(2, nutricionista.getId());
            return p.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
