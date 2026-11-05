package dao;

import connections.ConexaoMySQL;
import models.Consulta;
import models.Nutricionista;
import models.Paciente;

import java.sql.*;
import java.util.ArrayList;

public class ConsultaDAO {
    private static final String SELECT_BASE =
            "SELECT cons.idConsulta, cons.idNutricionista, cons.idPaciente, cons.dtConsulta, cons.hrConsulta, cons.dsConsulta, " +
                    "cons.stConsulta, cons.valorConsulta, cons.stPagamento, " +
                    "nutri.nmNome AS nutricionistaNome, nutri.nmEmail AS nutricionistaEmail, nutri.nmSenha AS nutricionistaSenha, nutri.stAtivo AS nutricionistaAtivo, " +
                    "pac.nmNome AS pacienteNome, pac.nmEmail AS pacienteEmail, pac.nmSenha AS pacienteSenha, pac.stAtivo AS pacienteAtivo " +
                    "FROM consulta cons JOIN nutricionista nutri ON nutri.idNutricionista=cons.idNutricionista " +
                    "JOIN paciente pac ON pac.idPaciente=cons.idPaciente AND pac.idNutricionista=cons.idNutricionista ";

    private Consulta mapear(ResultSet resultado) throws SQLException {
        Nutricionista nutricionista = new Nutricionista(resultado.getInt("idNutricionista"),
                resultado.getString("nutricionistaNome"), resultado.getString("nutricionistaEmail"),
                resultado.getString("nutricionistaSenha"), resultado.getBoolean("nutricionistaAtivo"));
        Paciente paciente = new Paciente(resultado.getInt("idPaciente"), nutricionista,
                resultado.getString("pacienteNome"), resultado.getString("pacienteEmail"),
                resultado.getString("pacienteSenha"), resultado.getBoolean("pacienteAtivo"));
        return new Consulta(resultado.getInt("idConsulta"), nutricionista, paciente, resultado.getDate("dtConsulta"),
                resultado.getTime("hrConsulta"), resultado.getString("dsConsulta"), resultado.getString("stConsulta"),
                resultado.getDouble("valorConsulta"), resultado.getString("stPagamento"));
    }

    public ArrayList<Consulta> selecionar(Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE cons.idNutricionista=? ORDER BY cons.dtConsulta,cons.hrConsulta");
            preparacao.setInt(1, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            ArrayList<Consulta> lista = new ArrayList<>();
            while (resultado.next()) lista.add(mapear(resultado));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Consulta selecionarPorId(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    SELECT_BASE + "WHERE cons.idConsulta=? AND cons.idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código da consulta é sequencial DENTRO de cada nutricionista: cada um começa no 1.
    // O próximo código é o maior código daquele nutricionista + 1.
    private Integer proximoId(Nutricionista nutricionista) throws SQLException {
        PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idConsulta) FROM consulta WHERE idNutricionista=?");
        preparacao.setInt(1, nutricionista.getId());
        ResultSet resultado = preparacao.executeQuery();
        resultado.next();
        return resultado.getInt(1) + 1; // nutricionista sem consultas: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Consulta consulta) {
        try {
            Integer id = proximoId(consulta.getNutricionista());
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO consulta(idNutricionista,idConsulta,idPaciente,dtConsulta,hrConsulta,dsConsulta,valorConsulta,stConsulta,stPagamento) VALUES(?,?,?,?,?,?,?,?,?)");
            preparacao.setInt(1, consulta.getNutricionista().getId());
            preparacao.setInt(2, id);
            preparacao.setInt(3, consulta.getPaciente().getId());
            preparacao.setDate(4, consulta.getData());
            preparacao.setTime(5, consulta.getHora());
            preparacao.setString(6, consulta.getDescricao());
            preparacao.setDouble(7, consulta.getValor());
            preparacao.setString(8, consulta.getStatus());
            preparacao.setString(9, consulta.getStatusPagamento());
            boolean inseriu = preparacao.executeUpdate() > 0;
            if (inseriu) consulta.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Consulta consulta) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET idPaciente=?,dtConsulta=?,hrConsulta=?,dsConsulta=?,stConsulta=?,valorConsulta=?,stPagamento=? WHERE idConsulta=? AND idNutricionista=?");
            preparacao.setInt(1, consulta.getPaciente().getId());
            preparacao.setDate(2, consulta.getData());
            preparacao.setTime(3, consulta.getHora());
            preparacao.setString(4, consulta.getDescricao());
            preparacao.setString(5, consulta.getStatus());
            preparacao.setDouble(6, consulta.getValor());
            preparacao.setString(7, consulta.getStatusPagamento());
            preparacao.setInt(8, consulta.getId());
            preparacao.setInt(9, consulta.getNutricionista().getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean cancelar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET stConsulta='Cancelada' WHERE idConsulta=? AND idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean pagar(Integer id, Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE consulta SET stPagamento='Pago' WHERE idConsulta=? AND idNutricionista=?");
            preparacao.setInt(1, id);
            preparacao.setInt(2, nutricionista.getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
