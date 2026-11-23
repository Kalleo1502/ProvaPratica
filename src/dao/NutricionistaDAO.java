package dao;

import connections.ConexaoMySQL;
import models.Nutricionista;

import java.sql.*;

public class NutricionistaDAO {
    private Nutricionista mapear(ResultSet resultado) throws SQLException {
        return new Nutricionista(resultado.getInt("idNutricionista"), resultado.getString("nmNome"),
                resultado.getString("nmEmail"), resultado.getString("nmSenha"), resultado.getBoolean("stAtivo"));
    }

    public Nutricionista selecionarPorEmail(String email) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "SELECT * FROM nutricionista WHERE nmEmail=? AND stAtivo=1");
            preparacao.setString(1, email);
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Nutricionista selecionarPorId(Integer id) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "SELECT * FROM nutricionista WHERE idNutricionista=?");
            preparacao.setInt(1, id);
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next() ? mapear(resultado) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // O código do nutricionista é sequencial (1, 2, 3...): o próximo é o maior código + 1.
    private Integer proximoId() throws SQLException {
        ResultSet resultado = ConexaoMySQL.get().prepareStatement(
                "SELECT MAX(idNutricionista) FROM nutricionista").executeQuery();
        resultado.next();
        return resultado.getInt(1) + 1; // tabela vazia: MAX é NULL, getInt devolve 0 e o primeiro código é 1
    }

    public Boolean inserir(Nutricionista nutricionista) {
        try {
            Integer id = proximoId();
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "INSERT INTO nutricionista(idNutricionista,nmNome,nmEmail,nmSenha,stAtivo) VALUES(?,?,?,?,1)");
            preparacao.setInt(1, id);
            preparacao.setString(2, nutricionista.getNome());
            preparacao.setString(3, nutricionista.getEmail());
            preparacao.setString(4, nutricionista.getSenha());
            boolean inseriu = preparacao.executeUpdate() > 0;
            if (inseriu) nutricionista.setId(id);
            return inseriu;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean atualizar(Nutricionista nutricionista) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE nutricionista SET nmNome=?,nmEmail=?,nmSenha=? WHERE idNutricionista=?");
            preparacao.setString(1, nutricionista.getNome());
            preparacao.setString(2, nutricionista.getEmail());
            preparacao.setString(3, nutricionista.getSenha());
            preparacao.setInt(4, nutricionista.getId());
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletar(Integer id) {
        try {
            PreparedStatement preparacao = ConexaoMySQL.get().prepareStatement(
                    "UPDATE nutricionista SET stAtivo=0 WHERE idNutricionista=?");
            preparacao.setInt(1, id);
            return preparacao.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
